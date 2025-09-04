package com.example.workerapp.view.income

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.workerapp.R
import com.example.workerapp.data.model.TransactionModel
import com.example.workerapp.utils.TimeUtils

sealed class IncomeSection {
    object SwitchText : IncomeSection()
    object Turnover : IncomeSection()
    data class TransactionHistory(val transaction: List<TransactionModel>) : IncomeSection()
}

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
        IncomeSection.SwitchText,
        IncomeSection.Turnover,
        IncomeSection.TransactionHistory(transactionHistory)
    )

    LazyColumn(
        modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        item {
            Spacer(Modifier.height(24.dp))
        }

        incomeSection.forEach { section ->
            when (section) {
                is IncomeSection.SwitchText -> item {
                    SwitchTextAnimation()
                    Spacer(Modifier.height(24.dp))
                }

                is IncomeSection.Turnover -> item {
                    Turnover()
                    Spacer(Modifier.height(24.dp))
                }

                is IncomeSection.TransactionHistory -> item {
                    TransactionItem(section.transaction)
                    Spacer(Modifier.height(32.dp))
                }
            }
        }
    }
}

@Composable
fun SwitchTextAnimation(modifier: Modifier = Modifier) {

}

@Composable
fun Turnover(modifier: Modifier = Modifier) {
    Text(
        "255.000 $",
        modifier.fillMaxWidth(),
        textAlign = TextAlign.Center,
        fontSize = 24.sp,
        color = Color.Black, maxLines = 1
    )
}

@Composable
fun TransactionItem(items: List<TransactionModel> = emptyList()) {


    Column() {
        Text(
            stringResource(R.string.history_title),
            fontSize = 18.sp,
            color = Color.Black,
            fontWeight = FontWeight.W500
        )
        Spacer(Modifier.height(8.dp))
        items.forEach { item ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(item.id, fontSize = 14.sp, modifier = Modifier.weight(0.15f))
                Text(TimeUtils.formatDateTimeFull(item.date), fontSize = 14.sp)
                Spacer(Modifier.width(16.dp))
                Text(
                    item.amount.toString(),
                    fontSize = 14.sp,
                    modifier = Modifier.weight(1f),
                    color = if (item.amount < 0) Color.Red else Color(0xFF4CAF50),
                    maxLines = 1,
                    textAlign = TextAlign.End
                )
            }
        }
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