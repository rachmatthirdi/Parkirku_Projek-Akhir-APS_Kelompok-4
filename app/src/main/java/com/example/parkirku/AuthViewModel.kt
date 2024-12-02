package com.example.parkirku

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import android.content.Context
import android.content.Intent
import com.google.firebase.firestore.SetOptions


class AuthViewModel : ViewModel() {
    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun signIn(email: String, password: String, context: Context, onResult: (Boolean) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    val userId = user?.uid

                    val userDocRef = firestore.collection("users").document(userId!!)
                    userDocRef.get().addOnSuccessListener { document ->
                        if (document.exists()) {
                            val photoUrl = document.getString("photoUrl") ?: ""
                            val userMap = hashMapOf(
                                "name" to email.substringBefore("@"),
                                "email" to email,
                                "photoUrl" to photoUrl
                            )

                            // Simpan status login
                            val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                            with(sharedPreferences.edit()) {
                                putBoolean("is_logged_in", true)
                                putString("user_id", userId)
                                putString("photo_url", photoUrl)
                                apply()
                            }

                            userDocRef.set(userMap, SetOptions.merge())
                                .addOnSuccessListener {
                                    onResult(true)
                                }
                                .addOnFailureListener { e ->
                                    onResult(false)
                                }
                        } else {
                            onResult(false)
                        }
                    }.addOnFailureListener {
                        onResult(false)
                    }
                } else {
                    onResult(false)
                }
            }
    }

    fun signUp(email: String, password: String, photoUrl: String, context: Context, onResult: (Boolean) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    val userId = user?.uid

                    val userMap = hashMapOf(
                        "name" to email.substringBefore("@"),
                        "email" to email,
                        "photoUrl" to photoUrl
                    )

                    // Simpan status login
                    val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                    with(sharedPreferences.edit()) {
                        putBoolean("is_logged_in", true)
                        putString("user_id", userId)
                        putString("photo_url", photoUrl)
                        apply()
                    }

                    firestore.collection("users").document(userId!!)
                        .set(userMap, SetOptions.merge())
                        .addOnSuccessListener {
                            onResult(true)
                        }
                        .addOnFailureListener { e ->
                            onResult(false)
                        }
                } else {
                    onResult(false)
                }
            }
    }

    fun updateUserPhotoUrl(userId: String, photoUrl: String) {
        val userMap = mapOf("photoUrl" to photoUrl)
        firestore.collection("users").document(userId)
            .set(userMap, SetOptions.merge())
            .addOnSuccessListener {
                // URL foto berhasil diperbarui
            }
            .addOnFailureListener { e ->
                // Terjadi kesalahan
            }
    }


    fun sendPasswordResetEmail(email: String, onResult: (Boolean, String) -> Unit) {
        val auth = FirebaseAuth.getInstance()
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onResult(true, "Email reset password telah dikirim.")
                } else {
                    onResult(false, task.exception?.message ?: "Gagal mengirim email reset password.")
                }
            }
    }


    fun signOut(context: Context, onResult: () -> Unit) {
        auth.signOut()
        val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            clear()
            apply()
        }
        onResult()
        val intent = Intent(context, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }
}




