package pro.butovanton.noface

import com.google.firebase.auth.FirebaseAuth

class Auth {

    private var mAuth = FirebaseAuth.getInstance()

    init {
        isAuth()
    }

    fun isAuth() : Boolean {
        if (mAuth.currentUser == null)
                mAuth.signInAnonymously()
       return mAuth.currentUser != null
    }
}