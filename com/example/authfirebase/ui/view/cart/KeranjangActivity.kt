package com.example.authfirebase.ui.view.cart

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.authfirebase.R
import com.example.authfirebase.eventbus.UpdateCartEvent
import com.example.authfirebase.listener.ICartLoadListener
import com.example.authfirebase.listener.IDrinkLoadListener
import com.example.authfirebase.ui.adapter.MyDrinkAdapter
import com.example.authfirebase.ui.model.CartModel
import com.example.authfirebase.ui.model.DrinkModel
import com.example.authfirebase.util.SpaceItemDecoration
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_keranjang.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class KeranjangActivity : AppCompatActivity(), IDrinkLoadListener, ICartLoadListener {

    private lateinit var drinkLoadListener: IDrinkLoadListener
    private lateinit var cartLoadListener: ICartLoadListener

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        if (EventBus.getDefault().hasSubscriberForEvent(UpdateCartEvent::class.java))
            EventBus.getDefault().removeStickyEvent(UpdateCartEvent::class.java)
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public fun onUpdateCartEvent(event: UpdateCartEvent){
        countCartFromFirebase()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_keranjang)

        supportActionBar?.hide()
        init()
        loadDrinkFromFirebase()
        countCartFromFirebase()
    }

    private fun countCartFromFirebase() {
        val cartModels: MutableList<CartModel> = ArrayList()
        FirebaseDatabase.getInstance()
            .getReference("Cart")
            .child("UNIQUE_USER_ID")
            .addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (cartSnaphot in snapshot.children){
                        val cartModel = cartSnaphot.getValue(CartModel::class.java)
                        cartModel!!.key = cartSnaphot.key
                        cartModels.add(cartModel)
                    }
                    cartLoadListener.onLoadCartSuccess(cartModels)
                }

                override fun onCancelled(error: DatabaseError) {
                    cartLoadListener.onLoadCartFailed(error.message)
                }

            })
    }

    private fun loadDrinkFromFirebase() {
        val drinkModels : MutableList<DrinkModel> = ArrayList()
        FirebaseDatabase.getInstance()
            .getReference("Drink")
            .addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        for (drinkSnapshot in snapshot.children){
                            val drinkModel = drinkSnapshot.getValue(DrinkModel::class.java)
                            drinkModel!!.key = drinkSnapshot.key
                            drinkModels.add(drinkModel)
                        }
                        drinkLoadListener.onDrinkLoadSuccess(drinkModels)
                    }
                    else{
                        drinkLoadListener.onDrinkLoadFailed("Drink items not exists")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    drinkLoadListener.onDrinkLoadFailed(error.message)
                }

            })
    }

    private fun init(){
        drinkLoadListener = this
        cartLoadListener = this

        val gridLayoutManager = GridLayoutManager(this, 2)
        recycler_drink.layoutManager = gridLayoutManager
        recycler_drink.addItemDecoration(SpaceItemDecoration())

        btnCart.setOnClickListener { startActivity(Intent(this, CartActivity::class.java)) }
    }

    override fun onDrinkLoadSuccess(drinkModelList: List<DrinkModel>) {
        val adapter = MyDrinkAdapter(this, drinkModelList!!, cartLoadListener)
        recycler_drink.adapter = adapter
    }

    override fun onDrinkLoadFailed(message: String?) {
        Snackbar.make(keranjangLayout,message!!, Snackbar.LENGTH_LONG).show()
    }

    override fun onLoadCartSuccess(cartModelList: List<CartModel>) {
        var cartSum = 0
        for (cartModel in cartModelList!!) cartSum += cartModel!!.quantity
        badge.setNumber(cartSum)
    }

    override fun onLoadCartFailed(message: String?) {
        Snackbar.make(keranjangLayout,message!!, Snackbar.LENGTH_LONG).show()
    }
}