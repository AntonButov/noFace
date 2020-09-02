package pro.butovanton.noface

import com.google.firebase.database.*
import javax.inject.Singleton

@Singleton
class RepoTest(override var ref : DatabaseReference) : Repo(ref)