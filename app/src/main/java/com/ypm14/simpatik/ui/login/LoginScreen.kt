package com.ypm14.simpatik.ui.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ypm14.simpatik.data.SimpatikRepo
import com.ypm14.simpatik.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit, onRegister: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPass by remember { mutableStateOf(false) }
    var rememberMe by remember { mutableStateOf(false) }
    var loading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    Box(Modifier.fillMaxSize().background(Background)) {
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
                .padding(top = 56.dp, bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // ── Branding ──
            Box(
                Modifier.size(88.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Primary.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.School, null, tint = Primary, modifier = Modifier.size(48.dp))
            }
            Spacer(Modifier.height(16.dp))
            Text(
                "SIMPATIK 14",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                letterSpacing = 1.sp
            )
            Spacer(Modifier.height(6.dp))
            Text("Masuk ke akun Anda", fontSize = 15.sp, color = TextSecondary)
            Spacer(Modifier.height(36.dp))

            // ── Form Card ──
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Email
                    OutlinedTextField(
                        email, { email = it },
                        label = { Text("Email") },
                        leadingIcon = { Icon(Icons.Default.Email, null, tint = TextSecondary) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Primary,
                            unfocusedBorderColor = TextSecondary.copy(alpha = 0.3f),
                            focusedContainerColor = Background,
                            unfocusedContainerColor = Background
                        )
                    )

                    // Password
                    OutlinedTextField(
                        password, { password = it },
                        label = { Text("Password") },
                        leadingIcon = { Icon(Icons.Default.Lock, null, tint = TextSecondary) },
                        trailingIcon = {
                            IconButton({ showPass = !showPass }) {
                                Icon(if (showPass) Icons.Default.VisibilityOff else Icons.Default.Visibility, null, tint = TextSecondary)
                            }
                        },
                        visualTransformation = if (showPass) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Primary,
                            unfocusedBorderColor = TextSecondary.copy(alpha = 0.3f),
                            focusedContainerColor = Background,
                            unfocusedContainerColor = Background
                        )
                    )

                    // Remember Me + Lupa Password
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = rememberMe,
                                onCheckedChange = { rememberMe = it },
                                colors = CheckboxDefaults.colors(checkedColor = Primary)
                            )
                            Text("Ingat saya", fontSize = 13.sp, color = TextSecondary)
                        }
                        Text(
                            "Lupa Password?",
                            fontSize = 13.sp,
                            color = Primary,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.clickable { /* TODO */ }
                        )
                    }

                    // Error
                    if (error != null) {
                        Text(error!!, color = Danger, fontSize = 13.sp, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                    }

                    // Tombol Masuk
                    Button(
                        onClick = {
                            if (email.isBlank() || password.isBlank()) { error = "Email dan password harus diisi"; return@Button }
                            loading = true; error = null
                            scope.launch {
                                SimpatikRepo.login(email.trim(), password)
                                    .onSuccess { loading = false; onLoginSuccess() }
                                    .onFailure { loading = false; error = it.message ?: "Login gagal" }
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Primary),
                        enabled = !loading
                    ) {
                        if (loading) CircularProgressIndicator(Modifier.size(20.dp), color = Color.White, strokeWidth = 2.dp)
                        else Text("Masuk", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                    }

                    // Divider
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        HorizontalDivider(Modifier.weight(1f), color = TextSecondary.copy(alpha = 0.2f))
                        Text(" atau ", fontSize = 13.sp, color = TextSecondary, modifier = Modifier.padding(horizontal = 8.dp))
                        HorizontalDivider(Modifier.weight(1f), color = TextSecondary.copy(alpha = 0.2f))
                    }

                    // Google
                    OutlinedButton(
                        onClick = {
                            loading = true; scope.launch {
                                SimpatikRepo.loginGoogle()
                                    .onSuccess { loading = false; onLoginSuccess() }
                                    .onFailure { loading = false; error = "Login Google gagal" }
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimary),
                        enabled = !loading
                    ) {
                        Icon(Icons.Default.AccountCircle, null, tint = Danger, modifier = Modifier.size(22.dp))
                        Spacer(Modifier.width(10.dp))
                        Text("Login dengan Google", fontSize = 14.sp)
                    }

                    // Microsoft
                    OutlinedButton(
                        onClick = {
                            loading = true; scope.launch {
                                SimpatikRepo.loginMicrosoft()
                                    .onSuccess { loading = false; onLoginSuccess() }
                                    .onFailure { loading = false; error = "Login Microsoft gagal" }
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimary),
                        enabled = !loading
                    ) {
                        Icon(Icons.Default.AccountCircle, null, tint = Primary, modifier = Modifier.size(22.dp))
                        Spacer(Modifier.width(10.dp))
                        Text("Login dengan Microsoft", fontSize = 14.sp)
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            // Register
            Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                Text("Belum punya akun? ", fontSize = 14.sp, color = TextSecondary)
                Text("Daftar", fontSize = 14.sp, color = Primary, fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable { onRegister() })
            }
        }
    }
}
