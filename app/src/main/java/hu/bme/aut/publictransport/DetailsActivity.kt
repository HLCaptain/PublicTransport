package hu.bme.aut.publictransport

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.DatePicker
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.util.Pair
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import hu.bme.aut.publictransport.ui.theme.PublicTransportTheme
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import kotlin.time.Duration.Companion.days
import kotlin.time.DurationUnit

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
                        ticketType = intent.getIntExtra(
                            TicketTypeKey,
                            UnknownType
                        )
                    )
                }
            }
        }
    }

    companion object {
        const val TicketTypeKey = "TicketTypeKey"

        const val UnknownType = 0
        const val BikeType = 1
        const val BusType = 2
        const val TrainType = 3
        const val BoatType = 4

        const val BikePrice = 700L
        const val BusPrice = 1000L
        const val TrainPrice = 1500L
        const val BoatPrice = 2500L

        const val FullPriceType = 1
        const val SeniorType = 2
        const val StudentType = 3

        const val FullPriceMultiplier = 1f
        const val SeniorMultiplier = .1f
        const val StudentMultiplier = .5f
    }
}

@Preview(showBackground = true)
@Composable
fun DetailsScreen(
    ticketType: Int = DetailsActivity.UnknownType
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize()
            .scrollable(
                state = scrollState,
                orientation = Orientation.Vertical
            ),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        DetailsActivity.apply {
            val ticketTypeText = when (ticketType) {
                BusType -> stringResource(R.string.bus_ticket)
                BikeType -> stringResource(R.string.bike_ticket)
                BoatType -> stringResource(R.string.boat_ticket)
                TrainType -> stringResource(R.string.train_ticket)
                else -> stringResource(R.string.unknown_ticket_type)
            }
            Text(
                text = ticketTypeText,
                style = MaterialTheme.typography.headlineMedium
            )
        }
        val context = LocalContext.current
        // Not using Material 2 Designed Date Range Dialog fragment
//        Text(
//            text = stringResource(R.string.start_date),
//            style = MaterialTheme.typography.labelLarge
//        )
//        var startDate by remember { mutableStateOf(LocalDate.now()) }
//        DatePickerButton(
//            context = context,
//            date = startDate,
//            onDateChangedListener = { _: DatePicker, year, month, day ->
//                startDate = LocalDate.of(year, month, day)
//            },
//            text = startDate.toString()
//        )
//        Text(
//            text = stringResource(R.string.end_date),
//            style = MaterialTheme.typography.labelLarge
//        )
//        var endDate by remember { mutableStateOf(LocalDate.now()) }
//        DatePickerButton(
//            context = context,
//            date = endDate,
//            onDateChangedListener = { _: DatePicker, year, month, day ->
//                endDate = LocalDate.of(year, month, day)
//            },
//            text = endDate.toString()
//        )
        // Using Material 2 Designed Date Range Dialog fragment
        var startInstant by remember { mutableStateOf(Instant.now()) }
        var endInstant by remember {
            mutableStateOf(
                Instant.now().plusMillis(1.days.toLong(DurationUnit.MILLISECONDS))
            )
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = CenterVertically
        ) {
            Text(
                text = stringResource(R.string.select_date_range),
                style = MaterialTheme.typography.labelLarge
            )
            val startDate = startInstant.atZone(ZoneId.systemDefault()).toLocalDate()
            val endDate = endInstant.atZone(ZoneId.systemDefault()).toLocalDate()
            Text(
                text = "$startDate - $endDate",
                style = MaterialTheme.typography.bodySmall
            )
        }
        DateRangePickerButton(
            context = context,
            startInstant = startInstant,
            endInstant = endInstant,
            onPositiveButtonListener = {
                startInstant = Instant.ofEpochMilli(it.first)
                endInstant = Instant.ofEpochMilli(it.second)
            }
        )

        Text(
            text = stringResource(R.string.price_category),
            style = MaterialTheme.typography.labelLarge
        )
        var selected by remember { mutableStateOf(DetailsActivity.FullPriceType) }
        DetailsActivity.apply {
            DetailsRadioGroup(
                options = hashMapOf(
                    FullPriceType to stringResource(R.string.full_price),
                    SeniorType to stringResource(R.string.senior),
                    StudentType to stringResource(R.string.student)
                ),
                selected = selected,
                onOptionSelectedChange = { selected = it },
            )

            val basePrice = when (ticketType) {
                BusType -> BusPrice
                BikeType -> BikePrice
                TrainType -> TrainPrice
                BoatType -> BoatPrice
                else -> maxOf(BusPrice, BikePrice, TrainPrice, BoatPrice)
            }
            val priceMultiplier = when (selected) {
                FullPriceType -> FullPriceMultiplier
                SeniorType -> SeniorMultiplier
                StudentType -> StudentMultiplier
                else -> FullPriceMultiplier
            }
            val price = basePrice * priceMultiplier
            Text(
                text = price.toString(),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineSmall
            )
        }
        Button(
            onClick = {
                val startDate = startInstant.atZone(ZoneId.systemDefault()).toLocalDate()
                val endDate = endInstant.atZone(ZoneId.systemDefault()).toLocalDate()
                val dateString = "$startDate - $endDate"
                val intent = Intent(context, PassActivity::class.java)
                    .putExtra(PassActivity.TravelTypeKey, ticketType)
                    .putExtra(PassActivity.DateKey, dateString)
                context.startActivity(intent)
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = stringResource(R.string.purchase_pass))
        }
    }
}

/**
 * Date picker button opens a DatePickerDialog
 * with a given date and calls onDateChangedListener
 * when the selected date is changed.
 *
 * @param context
 * @param text
 * @param date
 * @param onDateChangedListener
 * @receiver
 */
@Composable
fun DatePickerButton(
    context: Context = LocalContext.current,
    text: String = "Pick a date",
    date: LocalDate = LocalDate.now(),
    onDateChangedListener: (DatePicker, Int, Int, Int) -> Unit =
        { _: DatePicker, _: Int, _: Int, _: Int -> }
) {
    val datePickerDialog = DatePickerDialog(
        context,
        onDateChangedListener,
        date.year, date.monthValue, date.dayOfMonth
    )

    Button(onClick = {
        datePickerDialog.show()
    }) {
        Text(text = text)
    }
}

@Composable
fun DateRangePickerButton(
    context: Context = LocalContext.current,
    text: String = "Pick a date",
    startInstant: Instant = Instant.now(),
    endInstant: Instant = startInstant.plusMillis(1.days.toLong(DurationUnit.MILLISECONDS)),
    onPositiveButtonListener: (Pair<Long, Long>) -> Unit =
        { _: Pair<Long, Long> -> }
) {
    endInstant.coerceAtLeast(startInstant.plusMillis(1.days.toLong(DurationUnit.MILLISECONDS)))
    val constraints = CalendarConstraints.Builder()
        .setValidator(DateValidatorPointForward.now())
        .build()
    val datePickerDialog = MaterialDatePicker.Builder.dateRangePicker()
        .setTitleText(stringResource(R.string.start_date_end_date))
        .setSelection(
            Pair(
                startInstant.toEpochMilli(),
                endInstant.toEpochMilli()
            )
        )
        .setCalendarConstraints(constraints)
        .build()
    datePickerDialog.addOnPositiveButtonClickListener(onPositiveButtonListener)

    Button(
        onClick = {
            datePickerDialog.show(
                (context as AppCompatActivity).supportFragmentManager,
                datePickerDialog.toString()
            )
        }) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = Icons.Default.DateRange.name
            )
            Text(text = text)
        }
    }
}

@Composable
inline fun <reified Option : Any, reified NullableOption : Option?> DetailsRadioGroup(
    options: HashMap<Option, String>,
    selected: NullableOption,
    crossinline onOptionSelectedChange: (Option) -> Unit = {},
) {
    DetailsRadioGroup(
        options = options.keys.toList(),
        selected = selected,
        onOptionSelectedChange = onOptionSelectedChange,
        optionToRadioButtonText = { options[it] ?: it.toString() }
    )
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
                verticalAlignment = CenterVertically,
            ) {
                RadioButton(
                    selected = (option == selected),
                    onClick = { onOptionSelectedChange(option) },
                )
                Text(text = optionToRadioButtonText(option))
            }
        }
    }
}
