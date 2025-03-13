package com.hye.sesac.klangpj.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.lifecycle.lifecycleScope
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.hye.sesac.klangpj.MainActivity
import com.hye.sesac.klangpj.common.throttleFirst
import com.hye.sesac.klangpj.databinding.ActivityLoginBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.ldralighieri.corbind.view.clicks


const val TAG ="LoginActivity"
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var credentialManager: CredentialManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityLoginBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }
        ViewCompat.setOnApplyWindowInsetsListener(window.decorView.rootView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = FirebaseAuth.getInstance()
        credentialManager = CredentialManager.create(this)

        binding.loginBtn.clicks()
            .throttleFirst(300L)
            .onEach {

                signIn()
            }
            .launchIn(lifecycleScope)
    }

    private  fun signIn() {
        lifecycleScope.launch {
            try {
                val result = getGoogleCredential()
                //firebase autoupdate
                val credential = GoogleAuthProvider.getCredential(result.idToken, null)

                auth.signInWithCredential(credential).addOnCompleteListener { task->
                    if(task.isSuccessful){
                        val user = auth.currentUser

                        user?.let {

                            val userId = user.uid
                            val userEmail= user.email ?:""
                            val userPhotoUrl = user.photoUrl.toString()
                            saveUserToFireStore(userId, userEmail, userPhotoUrl)

                            val intent = Intent(this@LoginActivity, MainActivity::class.java).also {
                                it.putExtra("userEmail",userEmail)
                            }
                            startActivity(intent)
                            finish()
                        }

                    }else{
                    }
                }



            }catch (e:Exception){
                Log.e(com.hye.sesac.klangpj.ui.TAG, "signIn: ", e)
            }
        }

    }


    /**
     * google idToken 얻기
     */
    private fun getGoogleIdOptions(): GetGoogleIdOption {
        return GetGoogleIdOption.Builder()
            //여러 구글 아이디 중 기존에 선택한 아이디 가져오기
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId("108222104739-gtkebvqfba9kodgfdngufuunu8ud996g.apps.googleusercontent.com")
            //이전에 선택한 계정을 자동으로 선택하는 옵션
            .setAutoSelectEnabled(true)
            .setNonce(null)
            .build()
    }

    /**
     * user's credential 가져오기
     */

    private fun getCredentialRequest(googleIdOption: GetGoogleIdOption): GetCredentialRequest {
        return GetCredentialRequest(
            listOf(googleIdOption)
        )

    }

    private suspend fun getGoogleCredential() : GoogleIdTokenCredential {
        val result = credentialManager.getCredential(
            this,
             getCredentialRequest(getGoogleIdOptions())
        )
        return GoogleIdTokenCredential.createFrom(result.credential.data)
    }

    private fun saveUserToFireStore(
        uid: String,
        email: String,
        photoUrl: String,
    ){
        val db = Firebase.firestore
        val userRef = db.collection("users").document(uid)

        val userInfo = hashMapOf(
            "uid" to uid,
            "email" to email,
            "photoUrl" to photoUrl
        )

        userRef.set(userInfo)

    }





}