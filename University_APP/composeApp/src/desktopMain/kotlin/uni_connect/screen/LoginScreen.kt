package uni_connect.screen

import ContentWithMessageBar
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import io.github.jan.supabase.gotrue.auth
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import rememberMessageBarState
import uni_connect.Database.signInWithEmail
import uni_connect.Database.signUpNewUserWithEmail
import uni_connect.Database.supabase
import university_connect.composeapp.generated.resources.Res
import university_connect.composeapp.generated.resources.logo

class LoginScreen: Screen {


    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val messageBarState = rememberMessageBarState()

        val scope = rememberCoroutineScope()
        val auth = remember { supabase.auth }
        var userEmail by remember { mutableStateOf("") }
        var userPassword by remember { mutableStateOf("") }
        var errorMessage by remember { mutableStateOf<String?>(null) }

        ContentWithMessageBar(messageBarState = messageBarState){
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Image(painter = painterResource(Res.drawable.logo), modifier = Modifier.size(150.dp), contentDescription = "")

                Spacer(modifier = Modifier.height(25.dp))

                OutlinedTextField(
                    value = userEmail,
                    onValueChange = { userEmail = it },
                    label = { Text("Email") },
                    leadingIcon = {Icon(imageVector = Icons.Filled.Email, contentDescription = "Email") },
                    isError = errorMessage != null,
                    supportingText = {
                        if (errorMessage != null) {
                            Text(
                                text = errorMessage!!,
                                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.error),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    },
                )

                Spacer(modifier = Modifier.height(12.dp))


                OutlinedTextField(
                    value = userPassword,
                    onValueChange = { newPassword ->
                        userPassword = newPassword
                        // Trigger validation when the password changes
                        errorMessage = if (newPassword.length < 6 && newPassword.isNotEmpty()) {
                            "Min.length is 6 characters"
                        } else {
                            null
                        }
                    },
                    label = { Text("Password") },
                    leadingIcon = { Icon(imageVector = Icons.Filled.Info, contentDescription = "Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    isError = errorMessage != null,
                    supportingText = {
                        if (errorMessage != null) {
                            Text(
                                text = errorMessage!!,
                                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.error),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    },
                )

                Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            scope.launch {
                                try {
                                    signUpNewUserWithEmail(userEmail, userPassword)
                                    // Check if the user is logged in after sign-up
                                    val currentSession = auth.currentSessionOrNull()
                                    if (currentSession != null) {
                                        //println(currentSession)
                                        messageBarState.addSuccess("Successfully created account in")
                                        delay(1500L)
                                        navigator.replace(HomeScreen())
                                    }
                                } catch (e: Exception) {
                                    try {
                                        signInWithEmail(userEmail, userPassword)
                                        val currentSession = auth.currentSessionOrNull()
                                        if (currentSession != null) {
                                            //println(currentSession)
                                            messageBarState.addSuccess("Successfully logged in")
                                            delay(1500L)
                                            navigator.replace(HomeScreen())
                                        }
                                    } catch (e: Exception) {
                                        errorMessage = "Invalid Credentials"
                                        messageBarState.addError(Exception("Login failed"))
                                    }
                                }
                            }
                        }
                    ){
                        Text(text = "Sign In")
                    }
            }
        }
    }
}
