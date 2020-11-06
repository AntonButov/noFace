package pro.butovanton.noface

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import pro.butovanton.noface.di.App

class Auth {

    private var mAuth = FirebaseAuth.getInstance()

    init {
        isAuth()
    }

    fun isAuth() : Boolean {
        if (mAuth.currentUser == null)
                mAuth.signInAnonymously().addOnCompleteListener {
                    Log.d(App.TAG, "Новый анонимный пользователь.")
                }

       return mAuth.currentUser != null
    }
}