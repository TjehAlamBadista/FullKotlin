package com.example.authfirebase.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.authfirebase.R
import com.example.authfirebase.eventbus.UpdateCartEvent
import com.example.authfirebase.listener.ICartLoadListener
import com.example.authfirebase.listener.IRecyclerClickListener
import com.example.authfirebase.ui.model.CartModel
import com.example.authfirebase.ui.model.DrinkModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.greenrobot.eventbus.EventBus
import java.lang.StringBuilder

class MyDrinkAdapter(
    private val context: Context, private val list: List<DrinkModel>, private val cartListener: ICartLoadListener
    ) : RecyclerView.Adapter<MyDrinkAdapter.MyDrinkViewHolder>() {

    class MyDrinkViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var imageView: ImageView? = null
        var txtName: TextView? = null
        var txtPrice: TextView? = null

        private var clickListener: IRecyclerClickListener? = null
        fun setClickListener(clickListener: IRecyclerClickListener){
            this.clickListener = clickListener
        }

        init {
            imageView = itemView.findViewById(R.id.imageView) as ImageView
            txtName = itemView.findViewById(R.id.txtName) as TextView
            txtPrice = itemView.findViewById(R.id.txtPrice) as TextView

            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            clickListener!!.onItemClickListener(v, adapterPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyDrinkViewHolder {
        return MyDrinkViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_drink_item, parent, false))
    }

    override fun onBindViewHolder(holder: MyDrinkViewHolder, position: Int) {
        Glide.with(context)
            .load(list[position].image)
            .into(holder.imageView!!)
        holder.txtName!!.text = StringBuilder().append(list[position].name)
        holder.txtPrice!!.text = StringBuilder("$").append(list[position].price)

        holder.setClickListener(object : IRecyclerClickListener{
            override fun onItemClickListener(view: View?, position: Int) {
                addCart(list[position])
            }

        })
    }

    private fun addCart(drinkModel: DrinkModel) {
        val userCart = FirebaseDatabase.getInstance()
            .getReference("Cart")
            .child("UNIQUE_USER_ID")

        userCart.child(drinkModel.key!!)
            .addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        val cartmodel = snapshot.getValue(CartModel::class.java)
                        val updateData : MutableMap<String, Any> = HashMap()

                        cartmodel!!.quantity = cartmodel!!.quantity + 1
                        updateData["quantity"] = cartmodel!!.quantity
                        updateData["totalPrice"] = cartmodel!!.quantity * cartmodel!!.price!!.toFloat()

                        userCart.child(drinkModel.key!!)
                            .updateChildren(updateData)
                            .addOnSuccessListener {
                                EventBus.getDefault().postSticky(UpdateCartEvent())
                                cartListener.onLoadCartFailed("Success add to cart")
                            }
                            .addOnFailureListener { e -> cartListener.onLoadCartFailed(e.message) }
                    }
                    else{
                        val cartModel = CartModel()
                        cartModel.key = drinkModel.key
                        cartModel.name = drinkModel.name
                        cartModel.image = drinkModel.image
                        cartModel.price = drinkModel.price
                        cartModel.quantity = 1
                        cartModel.totalPrice = drinkModel.price!!.toFloat()

                        userCart.child(drinkModel.key!!)
                            .setValue(cartModel)
                            .addOnSuccessListener {
                                EventBus.getDefault().postSticky(UpdateCartEvent())
                                cartListener.onLoadCartFailed("Success add to cart")
                        }
                            .addOnFailureListener { e -> cartListener.onLoadCartFailed(e.message) }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }

    override fun getItemCount(): Int {
        return list.size
    }
}