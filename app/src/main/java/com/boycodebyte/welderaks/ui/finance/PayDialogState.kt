package com.boycodebyte.welderaks.ui.finance

import android.icu.util.Calendar

class PayDialogState(
    val paymentState: PaymentState,
    val calendar: Calendar,
    val id: Int,
    val hourlyPayment: Int
)
