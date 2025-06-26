package com.acteam.vocago.presentation.screen.user.profile

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.acteam.vocago.R
import com.acteam.vocago.presentation.screen.auth.register.component.CommonTextField
import com.acteam.vocago.presentation.screen.common.LoadingSurface
import com.acteam.vocago.presentation.screen.common.data.UIState
import com.acteam.vocago.presentation.screen.user.profile.component.AvatarPickerBottomSheet
import com.acteam.vocago.presentation.screen.user.profile.component.DateField
import com.acteam.vocago.presentation.screen.user.profile.component.ModernButton
import com.acteam.vocago.utils.responsiveDP
import java.io.File
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    navController: NavController,
) {
    var editMode by remember { mutableStateOf(false) }
    val user by viewModel.userProfile.collectAsState()
    val userUpdate by viewModel.updateUserState.collectAsState()
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val placeholderPainter = painterResource(id = R.drawable.capybara_avatar)
    val imageRequest = remember(user?.avatar, context) {
        ImageRequest.Builder(context)
            .data(user?.avatar)
            .crossfade(true)
            .diskCachePolicy(CachePolicy.ENABLED)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .build()
    }
    var boxHeight by remember { mutableIntStateOf(0) }
    val avatarSize = responsiveDP(mobile = 160, tabletPortrait = 200, tabletLandscape = 200)
    var isShowModelChooseAvatar by remember {
        mutableStateOf(false)
    }
    var isShowAvatarDialog by remember {
        mutableStateOf(false)
    }
    var showMenu by remember {
        mutableStateOf(false)
    }
    var capturedImageUri by remember { mutableStateOf<Uri?>(null) }


    val (year, month, dayOfMonth) = remember {
        val dob = if (editMode) userUpdate.dob else user?.dob

        if (!dob.isNullOrBlank()) {
            try {
                val localDate = LocalDate.parse(dob)
                Triple(localDate.year, localDate.monthValue, localDate.dayOfMonth)
            } catch (e: Exception) {
                Triple(2000, 1, 1) // fallback nếu parse sai
            }
        } else {
            Triple(2000, 1, 1) // fallback nếu dob rỗng
        }
    }



    LaunchedEffect(
        Unit
    ) {
        viewModel.syncProfile()
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (success) {
                capturedImageUri?.let { uri ->
                    viewModel.updateAvatar(uri, context)
                    isShowModelChooseAvatar = false
                }
            }
        }
    )


    val permissionCamera = Manifest.permission.CAMERA


    val permissionGallery = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.READ_MEDIA_IMAGES
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            viewModel.updateAvatar(it, context)
        }
    }

    val firstNameFocusRequester = remember { FocusRequester() }
    val lastNameFocusRequester = remember { FocusRequester() }
    val addressFocusRequester = remember { FocusRequester() }

    val requestGalleryPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            galleryLauncher.launch("image/*")
        } else {
            Toast.makeText(
                context,
                context.getString(R.string.permission_denied),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    val requestCameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        try {
            if (isGranted) {
                val photoFile = File(context.externalCacheDir, "temp_image.jpg")
                capturedImageUri = FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.fileprovider",
                    photoFile
                )
                capturedImageUri?.let { uri ->
                    cameraLauncher.launch(uri)
                }
            } else {
                Toast.makeText(
                    context,
                    context.getString(R.string.permission_denied),
                    Toast.LENGTH_SHORT
                ).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    if (user == null || uiState is UIState.UILoading) Box(
        Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        LoadingSurface()
    } else Column(
        modifier = Modifier
            .padding(
                8.dp
            )
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(
            responsiveDP(
                mobile = 8,
                tabletPortrait = 16,
                tabletLandscape = 16
            )
        )
    ) {
        Row(
            Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    navController.popBackStack()
                }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(
                modifier = Modifier.weight(1f)
            )
            Box {
                IconButton(onClick = { showMenu = !showMenu }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "More options",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Devices") },
                        onClick = {

                        },
                        leadingIcon = {
                            Icon(Icons.Default.Devices, contentDescription = "Devices")
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Change Password") },
                        onClick = {

                        },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Lock,
                                contentDescription = "Change Password"
                            )
                        }
                    )
                }
            }
        }


        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(avatarSize)
                    .clip(CircleShape)
                    .clickable {
                        isShowModelChooseAvatar = true
                    },
                contentAlignment = Alignment.Center
            ) {
                if (user?.avatar.isNullOrEmpty()) {
                    Image(
                        painter = placeholderPainter,
                        contentDescription = "User Avatar Placeholder",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    SubcomposeAsyncImage(
                        model = imageRequest,
                        contentDescription = "User Avatar Online",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        loading = {
                            CircularProgressIndicator(
                                modifier = Modifier.size(avatarSize * 0.5f)
                            )
                        },
                        error = {
                            Image(
                                painter = placeholderPainter,
                                contentDescription = "User Avatar Error",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                    )
                }
            }

            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .onGloballyPositioned { layoutCoordinates ->
                        boxHeight = layoutCoordinates.size.height
                    }
                    .offset { IntOffset(0, boxHeight / 2) }
                    .background(
                        MaterialTheme.colorScheme.surface, CircleShape
                    )
                    .clip(
                        CircleShape
                    )
                    .clickable {
                        if (ContextCompat.checkSelfPermission(
                                context,
                                permissionCamera
                            ) != PackageManager.PERMISSION_GRANTED
                        ) {
                            requestCameraPermissionLauncher.launch(permissionCamera)
                        } else {
                            val photoFile = File(context.externalCacheDir, "temp_image.jpg")

                            capturedImageUri = FileProvider.getUriForFile(
                                context,
                                "${context.packageName}.fileprovider",
                                photoFile
                            )
                            capturedImageUri?.let { uri ->
                                cameraLauncher.launch(uri)
                            }
                        }
                    },
            ) {
                Icon(
                    imageVector = Icons.Default.Camera,
                    contentDescription = "Camera",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(
                        40.dp
                    )
                )
            }
        }

        // Thông tin user
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp, start = 16.dp, end = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(
                        MaterialTheme.colorScheme.surfaceVariant,
                        RoundedCornerShape(12.dp)
                    )
                    .border(
                        1.dp, MaterialTheme.colorScheme.primary.copy(
                            alpha = 0.5f
                        ), RoundedCornerShape(12.dp)
                    )
            ) {
                Box(
                    modifier = Modifier
                        .width(48.dp)
                        .fillMaxHeight()
                        .background(
                            MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Face,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }

                OutlinedTextField(
                    value = if (!editMode) user?.firstName.orEmpty() else userUpdate.firstName,
                    onValueChange = {
                        viewModel.updateProfile(
                            userUpdate.copy(
                                firstName = it
                            )
                        )
                    },
                    placeholder = { Text(stringResource(R.string.input_first_name)) },
                    textStyle = MaterialTheme.typography.bodyMedium,
                    singleLine = true,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .focusRequester(
                            firstNameFocusRequester
                        ),
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = {
                        lastNameFocusRequester.requestFocus()
                    }),
                    readOnly = !editMode,
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color.Transparent,
                        focusedBorderColor = Color.Transparent,
                        disabledBorderColor = Color.Transparent
                    )
                )

                VerticalDivider(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(1.dp),
                    color = MaterialTheme.colorScheme.primary.copy(
                        alpha = 0.5f
                    )
                )

                OutlinedTextField(
                    value = if (!editMode) user?.lastName.orEmpty() else userUpdate.lastName,
                    onValueChange = {
                        viewModel.updateProfile(
                            userUpdate.copy(
                                lastName = it
                            )
                        )
                    },
                    placeholder = { Text(stringResource(R.string.input_last_name)) },
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .focusRequester(
                            lastNameFocusRequester
                        ),
                    readOnly = !editMode,
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = {
                        addressFocusRequester.requestFocus()
                    }),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color.Transparent,
                        focusedBorderColor = Color.Transparent,
                        disabledBorderColor = Color.Transparent
                    )
                )
            }

            CommonTextField(
                value = if (!editMode) user?.address.orEmpty() else userUpdate.address,
                onValueChange = {
                    viewModel.updateProfile(
                        userUpdate.copy(
                            address = it
                        )
                    )
                },
                placeholder = stringResource(R.string.input_enter_address),
                icon = Icons.Default.LocationOn,
                keyboardType = KeyboardType.Text,
                readOnly = !editMode,
                modifier = Modifier.focusRequester(
                    addressFocusRequester
                ),
            )




            DateField(
                year = year,
                month = month,
                dayOfMonth = dayOfMonth,
                readOnly = !editMode,
                onDateSelected = { y, m, d ->
                    // Handle date selected
                    val selectedDate = LocalDate.of(y, m, d)
                    val dobString = selectedDate.toString() // "yyyy-MM-dd"
                    viewModel.updateProfile(
                        userUpdate.copy(
                            dob = dobString
                        )
                    )
                }
            )
        }

        Spacer(
            modifier = Modifier.weight(1f)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            AnimatedVisibility(
                visible = !editMode,
                enter = slideInHorizontally(initialOffsetX = { fullWidth -> fullWidth }),
                exit = slideOutHorizontally(targetOffsetX = { fullWidth -> -fullWidth }),
                modifier = Modifier.fillMaxWidth()
            ) {
                ModernButton(
                    text = stringResource(R.string.btn_edit),
                    icon = Icons.Default.Edit,
                    onClick = {
                        editMode = true
                        firstNameFocusRequester.requestFocus()
                        viewModel.resetUpdateProfile()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }

            AnimatedVisibility(
                visible = editMode,
                enter = slideInHorizontally(initialOffsetX = { fullWidth -> -fullWidth }),
                exit = slideOutHorizontally(targetOffsetX = { fullWidth -> fullWidth }),
                modifier = Modifier.fillMaxWidth()
            ) {
                ModernButton(
                    text = stringResource(R.string.btn_save),
                    icon = Icons.Default.Save,
                    onClick = {
                        // Xử lý lưu dữ liệu tại đây
                        editMode = false // Quay lại chế độ xem
                        viewModel.updateProfileToServer()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.onPrimary,
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                )
            }
        }


        AvatarPickerBottomSheet(
            showBottomSheet = isShowModelChooseAvatar,
            onPickFromGallery = {
                if (ContextCompat.checkSelfPermission(
                        context,
                        permissionGallery
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    requestGalleryPermissionLauncher.launch(permissionGallery)
                } else {
                    galleryLauncher.launch("image/*")
                }
            },
            onViewAvatar = {
                isShowModelChooseAvatar = false
                isShowAvatarDialog = true
            },
            onTakePhoto = {
                if (ContextCompat.checkSelfPermission(
                        context,
                        permissionCamera
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    requestCameraPermissionLauncher.launch(permissionCamera)
                } else {
                    val photoFile = File(context.externalCacheDir, "temp_image.jpg")

                    capturedImageUri = FileProvider.getUriForFile(
                        context,
                        "${context.packageName}.fileprovider",
                        photoFile
                    )
                    capturedImageUri?.let { uri ->
                        cameraLauncher.launch(uri)
                    }
                }
            },
            onDismiss = {
                isShowModelChooseAvatar = false
            }
        )

        if (isShowAvatarDialog)
            Dialog(
                onDismissRequest = {
                    isShowAvatarDialog = false
                }
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(
                            avatarSize + 40.dp
                        ),
                    shape = MaterialTheme.shapes.medium
                ) {
                    SubcomposeAsyncImage(
                        model = imageRequest,
                        contentDescription = "User Avatar Online",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        loading = {
                            CircularProgressIndicator(
                                modifier = Modifier.size(avatarSize * 0.5f)
                            )
                        },
                        error = {
                            Image(
                                painter = placeholderPainter,
                                contentDescription = "User Avatar Error",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                    )
                }
            }
    }
}