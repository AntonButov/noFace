package pro.butovanton.noface

import pro.butovanton.noface.Models.User
import pro.butovanton.noface.Models.UserApp

class ValidaterUser(val muser: User, val muserApp: UserApp) {

    fun isUserValid(user: User, userApp: UserApp) : Boolean {
        return  isUserValidGender(user, userApp) &&
                isUserValidAge(user, userApp)
    }

    private fun isUserValidGender(user: User, userApp: UserApp) : Boolean{
        return (userApp.gender == muser.gender || userApp.gender == 2) &&
                (muserApp.gender == user.gender || muserApp.gender ==2)
    }

    private fun isUserValidAge(user: User, userApp: UserApp) : Boolean {
        var res : Boolean
        if (muser.gender == 2) res = true
        else res = muserApp.age[user.age] && userApp.age[muser.age]
        res = true //TODO временно отключили
        return res
    }
}