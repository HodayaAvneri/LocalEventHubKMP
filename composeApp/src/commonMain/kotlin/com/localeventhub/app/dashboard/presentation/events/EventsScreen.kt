package com.localeventhub.app.dashboard.presentation.events

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localeventhub.app.featurebase.common.Colors
import com.localeventhub.app.featurebase.presentation.ui.compose.TextTitleMedium
import localeventhub.composeapp.generated.resources.Res
import localeventhub.composeapp.generated.resources.add_photo
import localeventhub.composeapp.generated.resources.events
import localeventhub.composeapp.generated.resources.ic_add
import localeventhub.composeapp.generated.resources.ic_back
import localeventhub.composeapp.generated.resources.ic_menu
import localeventhub.composeapp.generated.resources.ic_profile
import localeventhub.composeapp.generated.resources.logo
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

data class Post(val username: String, val timeAgo: String, val content: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventsScreen(paddingValues: () -> PaddingValues, onNavigate: () -> Unit) {
    val posts = listOf(
        Post("test", "1 days ago", "test"),
        Post("test", "1 days ago", "test"),
        Post("test", "1 days ago", "test")
    )
    var expanded by remember { mutableStateOf(false) }
    val items = listOf("All", "Music Festival", "Food Festival", "Donors event")
    var selectedItem by remember { mutableStateOf(items[0]) }
    var sortExpanded by remember { mutableStateOf(false) }
    val sortItems = listOf("Select", "Distance")
    var sortSelectedItem by remember { mutableStateOf(sortItems[0]) }
    val platform = remember {
        com.localeventhub.app.getPlatform().name
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                modifier = Modifier.shadow(elevation = 5.dp),
                title = { Text(stringResource(Res.string.events), color = if(platform.startsWith("Android")) Color.White else Color.Black) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = if(platform.startsWith("Android")) Colors.primary else Color.White),
                actions = {
                    if(!platform.startsWith(prefix = "Android")){
                        IconButton(onClick = {onNavigate()}){
                            Icon(
                                painter = painterResource(Res.drawable.ic_add),
                                contentDescription = "Add",
                                tint = Color.Black
                            )
                        }
                    }
                }

            )
        },
        containerColor = Color.White,
        floatingActionButton = {
            if(platform.startsWith("Android")){
                FloatingActionButton(modifier = Modifier.padding(paddingValues()), onClick = {onNavigate()}){
                    Icon(painter = painterResource(Res.drawable.ic_add), contentDescription = null)
                }
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            Row(modifier = Modifier.fillMaxWidth().padding(top = 5.dp)) {
                Box(
                    modifier = Modifier.fillMaxWidth(0.5f).padding(start = 15.dp)
                        .clickable { expanded = true }) {
                    OutlinedTextField(
                        value = selectedItem,
                        onValueChange = { },
                        label = { Text("Filter", color = Color.Black) },
                        enabled = false,
                        colors = TextFieldDefaults.colors(
                            disabledTextColor = Color.Black,
                            disabledContainerColor = Color.White
                        ),
                        trailingIcon = {
                            Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                        }
                    )

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        items.forEach { item ->
                            DropdownMenuItem(
                                onClick = {
                                    selectedItem = item
                                    expanded = false
                                },
                                text = { Text(item) }
                            )
                        }
                    }
                }
                Box(
                    modifier = Modifier.fillMaxWidth().padding(start = 15.dp, end = 15.dp)
                        .clickable { sortExpanded = true }) {
                    OutlinedTextField(
                        value = sortSelectedItem,
                        onValueChange = { },
                        label = { Text("Sort", color = Color.Black) },
                        enabled = false,
                        colors = TextFieldDefaults.colors(
                            disabledTextColor = Color.Black,
                            disabledContainerColor = Color.White
                        ),
                        trailingIcon = {
                            Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                        }
                    )

                    DropdownMenu(
                        expanded = sortExpanded,
                        onDismissRequest = { sortExpanded = false }
                    ) {
                        sortItems.forEach { item ->
                            DropdownMenuItem(
                                onClick = {
                                    sortSelectedItem = item
                                    sortExpanded = false
                                },
                                text = { Text(item) }
                            )
                        }
                    }
                }
            }
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                items(posts) { post ->
                    PostItem(post)
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }

}

@Composable
fun PostItem(post: Post) {

    var menuExpand by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(8.dp)
        ) {
            // User info
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF90EE90)) // Light green for avatar placeholder
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(text = post.username, fontWeight = FontWeight.Bold)
                    Text(text = post.timeAgo, fontSize = 12.sp, color = Color.Gray)
                }
                Spacer(modifier = Modifier.weight(1f))
                Box {
                    IconButton(onClick = { menuExpand = true }) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_menu),
                            contentDescription = "More"
                        )
                    }

                    DropdownMenu(
                        expanded = menuExpand,
                        onDismissRequest = { menuExpand = false }
                    ) {
                        DropdownMenuItem(text = { Text(text = "View") }, onClick = { })
                        DropdownMenuItem(text = { Text(text = "Edit") }, onClick = { })
                        DropdownMenuItem(text = { Text(text = "Delete") }, onClick = { })
                    }
                }

            }

            Spacer(modifier = Modifier.height(8.dp))

            // Image placeholder
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF90EE90)) // Light green placeholder for image
            )

            Spacer(modifier = Modifier.height(8.dp))
            // Post content
            Text(text = post.content)
            Spacer(modifier = Modifier.height(4.dp))
            HorizontalDivider(color = Color.LightGray)
            // Action buttons
            Row {
                Button(
                    onClick = { /* Like action */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Like", color = Color.Black)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = { /* Comment action */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Comment", color = Color.Black)
                }
            }
        }
    }
}

