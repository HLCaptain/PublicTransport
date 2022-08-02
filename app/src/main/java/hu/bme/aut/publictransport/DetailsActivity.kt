package hu.bme.aut.publictransport

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.DatePicker
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import hu.bme.aut.publictransport.ui.theme.PublicTransportTheme
import java.util.*

class DetailsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PublicTransportTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DetailsScreen(
                        ticketType = intent.getStringExtra(TicketType)
                            ?: getString(R.string.bus_ticket)
                    )
                }
            }
        }
    }

    companion object {
        const val TicketType = "TICKET_TYPE"

        const val BoatPrice = 2500L
        const val BikePrice = 700L
        const val BusPrice = 1000L
        const val TrainPrice = 1500L

        const val FullPriceMultiplier = 1f
        const val SeniorPriceMultiplier = .1f
        const val PublicServantPriceMultiplier = .5f
    }
}

@Preview(showBackground = true)
@Composable
fun DetailsScreen(
    ticketType: String = "Ticket Type"
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .scrollable(
                state = scrollState,
                orientation = Orientation.Vertical
            )
    ) {
        Text(text = ticketType)
        Text(text = stringResource(R.string.start_date))
        val context = LocalContext.current
        var startDate by remember { mutableStateOf(Calendar.getInstance().time) }
        DatePickerButton(
            context = context,
            date = startDate,
            onDateChangedListener = { _: DatePicker, year, month, day ->
                // Sometimes it is way easier to use deprecated stuff...
                startDate = Date(year, month, day)
            },
            text = startDate.toString()
        )
        Text(text = stringResource(R.string.end_date))
        var endDate by remember { mutableStateOf(Calendar.getInstance().time) }
        DatePickerButton(
            context = context,
            date = endDate,
            onDateChangedListener = { _: DatePicker, year, month, day ->
                endDate = Date(year, month, day)
            },
            text = endDate.toString()
        )
        Text(text = stringResource(R.string.price_category))

        stringResource(id = R.string.full_price)
        var selected by remember { mutableStateOf(context.getString(R.string.full_price)) }
//        val selected = remember { mutableStateOf<String?>(null) }
        DetailsRadioGroup(
            options = listOf(
                stringResource(R.string.full_price),
                stringResource(R.string.senior),
                stringResource(R.string.public_servant)
            ),
            selected = selected,
            onOptionSelectedChange = { selected = it }
        )

        DetailsActivity.apply {
            val basePrice = when (ticketType) {
                stringResource(R.string.bus_ticket) -> BusPrice
                stringResource(R.string.bike_ticket) -> BikePrice
                stringResource(R.string.train_ticket) -> TrainPrice
                stringResource(R.string.boat_ticket) -> BoatPrice
                else -> maxOf(BusPrice, BikePrice, TrainPrice, BoatPrice)
            }
            val priceMultiplier = when (selected) {
                stringResource(R.string.full_price) -> FullPriceMultiplier
                stringResource(R.string.senior) -> SeniorPriceMultiplier
                stringResource(R.string.public_servant) -> PublicServantPriceMultiplier
                else -> FullPriceMultiplier
            }
            val price = basePrice * priceMultiplier
            Text(
                text = price.toString(),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }

        Button(
            onClick = {
                val travelType = selected
                val dateString = "$startDate - $endDate"

                val intent = Intent(context, PassActivity::class.java)
                    .putExtra(PassActivity.TravelTypeKey, travelType)
                    .putExtra(PassActivity.DateKey, dateString)
                context.startActivity(intent)
            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        ) {
            Text(text = stringResource(id = R.string.purchase_pass))
        }
    }
}

@Composable
fun DatePickerButton(
    context: Context = LocalContext.current,
    text: String = "Pick a date",
    date: Date = Calendar.getInstance().time,
    onDateChangedListener: (DatePicker, Int, Int, Int) -> Unit = { _: DatePicker, pickedYear: Int, pickedMonth: Int, pickedDay: Int ->
        date.time = Date(pickedYear, pickedMonth, pickedDay).time
        // With desugaring
//        date.value = Date.from(
//            LocalDate.of(pickedYear, pickedMonth, pickedDay)
//                .atStartOfDay(ZoneId.systemDefault())
//                .toInstant()
//        )
    }
) {
    // Declaring integer values
    // for year, month and day
    val year: Int
    val month: Int
    val day: Int

    // Initializing a Calendar
    val calendar = Calendar.getInstance()

    // Fetching current year, month and day
    year = calendar.get(Calendar.YEAR)
    month = calendar.get(Calendar.MONTH)
    day = calendar.get(Calendar.DAY_OF_MONTH)

    calendar.time = Date()

    // Declaring DatePickerDialog and setting
    // initial values as current values (present year, month and day)
    val datePickerDialog = DatePickerDialog(
        context,
        onDateChangedListener,
        year, month, day
    )

    Button(onClick = {
        datePickerDialog.show()
    }) {
        Text(text = text)
    }
}

@Composable
inline fun <reified Option : Any, reified NullableOption : Option?> DetailsRadioGroup(
    options: List<Option>,
    selected: NullableOption,
    crossinline onOptionSelectedChange: (Option) -> Unit = {},
    crossinline optionToRadioButtonText: (Option) -> String = { it.toString() }
) {
    Column {
        options.forEach { option ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = (option == selected),
                        onClick = { onOptionSelectedChange(option) }
                    ),
                verticalAlignment = CenterVertically
            ) {
                RadioButton(
                    selected = (option == selected),
                    onClick = { onOptionSelectedChange(option) },
                )
                Text(
                    text = optionToRadioButtonText(option),
                )
            }
        }
    }
}