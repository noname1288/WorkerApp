package com.example.workerapp.presentation.screens.income

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.workerapp.R
import com.example.workerapp.data.source.model.TransactionModel

sealed class IncomeSection {
    object Turnover : IncomeSection()
    data class TransactionHistory(val transaction: List<TransactionModel>) : IncomeSection()
}

enum class IncomeDestinationType() {
    Today, Week, Month
}

data class IncomeTabDestination(
    val label: String,
    val type: IncomeDestinationType
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IncomeScreen(modifier: Modifier = Modifier) {
    val transactionHistory = listOf(
        TransactionModel("1", 1719878400000, -150.0, "Grocery shopping"),
        TransactionModel("2", 1719792000000, -75.5, "Restaurant dinner"),
        TransactionModel("3", 1719705600000, 200.0, "Salary deposit"),
        TransactionModel("4", 1719619200000, -50.0, "Taxi fare"),
        TransactionModel("5", 1719532800000, -120.0, "Monthly subscription"),
        TransactionModel("6", 1719446400000, 300.0, "Freelance payment"),
        TransactionModel("7", 1719360000000, -500.0, "Rent payment"),
        TransactionModel("8", 1719273600000, 100.0, "Refund"),
        TransactionModel("9", 1719187200000, -90.0, "Online shopping"),
        TransactionModel("10", 1719100800000, 50.0, "Gift received")
    )

    val incomeSection = listOf(
        IncomeSection.Turnover,
        IncomeSection.TransactionHistory(transactionHistory)
    )

    val tabDestinations = listOf(
        IncomeTabDestination("Hôm nay", IncomeDestinationType.Today),
        IncomeTabDestination("Tuần", IncomeDestinationType.Week),
        IncomeTabDestination("Tháng", IncomeDestinationType.Month)
    )

    var selectedDestination by rememberSaveable { mutableIntStateOf(0) }

    LazyColumn(
        modifier
            .fillMaxSize()
    ) {
        item {
            //Top App Bar
            CenterAlignedTopAppBar(
                title = { Text("Thu nhập", fontWeight = FontWeight.Bold) },
                windowInsets = WindowInsets(0, 0, 0, 0),
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
            HorizontalDivider()
        }

        item {
            // Tabs
            PrimaryTabRow(
                selectedTabIndex = selectedDestination,
                containerColor = Color.White,
                indicator = {
                    TabRowDefaults.PrimaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(
                            selectedDestination,
                            matchContentSize = true
                        ),
                        color = colorResource(R.color.orange_primary),
                        width = Dp.Unspecified
                    )
                }
            ) {
                tabDestinations.forEachIndexed { index, destination ->
                    val isSelected = selectedDestination == index
                    Tab(
                        selected = isSelected,
                        onClick = { selectedDestination = index },
                        text = {
                            Text(
                                destination.label,
                                maxLines = 1,
                                color = if (isSelected) Color.Black else colorResource(R.color.subtext)
                            )
                        },
                    )
                }
            }

            Spacer(Modifier.height(12.dp))
        }

        // Content
        incomeSection.forEach { section ->
            when (section) {
                is IncomeSection.Turnover -> item {
                    Turnover()
                }

                is IncomeSection.TransactionHistory -> item {
                    Text(
                        "Lịch sử",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = colorResource(R.color.subtext)
                        ),
                        modifier = Modifier.padding(16.dp)
                    )
                    TransactionList(section.transaction)
                    Spacer(Modifier.height(32.dp))
                }
            }
        }
    }
}
@Composable
fun Turnover(modifier: Modifier = Modifier, content: String = "255.000.609 đ") {
    Box(
        Modifier
            .height(100.dp)
            .fillMaxWidth()
            .background(colorResource(R.color.light_orange)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            content,
            style = MaterialTheme.typography.headlineMedium.copy(
                color = colorResource(R.color.blue),
                fontWeight = FontWeight.Bold
            )
        )
    }

}

@Composable
fun TransactionList(items: List<TransactionModel> = emptyList()) {
    Column(Modifier.background(Color.White)) {
        Spacer(Modifier.height(8.dp))
        items.forEach { item ->
            TransactionItem()
        }
    }
}

@Composable
fun TransactionItem() {
    Column {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("21:30, 30/04/2022",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f))
            Text("150.000 đ",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = colorResource(R.color.subtext)
                ))
            IconButton(
                onClick = {}
            ) { Icon(Icons.Default.ArrowForwardIos, null, tint = colorResource(R.color.color_icon),
                modifier = Modifier.size(16.dp)) }
        }
        HorizontalDivider()
    }
}

@Preview(showBackground = true)
@Composable
fun PrevIncomeScreen(modifier: Modifier = Modifier) {
    val transactionHistory = listOf(
        TransactionModel("1", 1719878400000, -150.0, "Grocery shopping"),
        TransactionModel("2", 1719792000000, -75.5, "Restaurant dinner"),
        TransactionModel("3", 1719705600000, 200.0, "Salary deposit"),
        TransactionModel("4", 1719619200000, -50.0, "Taxi fare"),
        TransactionModel("5", 1719532800000, -120.0, "Monthly subscription"),
        TransactionModel("6", 1719446400000, 300.0, "Freelance payment"),
        TransactionModel("7", 1719360000000, -500.0, "Rent payment"),
        TransactionModel("8", 1719273600000, 100.0, "Refund"),
        TransactionModel("9", 1719187200000, -90.0, "Online shopping"),
        TransactionModel("10", 1719100800000, 50.0, "Gift received")
    )
    IncomeScreen()
}