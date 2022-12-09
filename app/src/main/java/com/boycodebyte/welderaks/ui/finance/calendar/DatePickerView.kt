package com.boycodebyte.welderaks.ui.finance.calendar

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Paint.Align
import android.graphics.Rect
import android.icu.text.DisplayContext
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.text.TextPaint
import android.text.format.DateFormat
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.boycodebyte.welderaks.R
import com.boycodebyte.welderaks.data.models.CalendarData
import java.util.*
import kotlin.math.min

const val DAYS_IN_WEEK = 7
const val MAX_WEEKS_IN_MONTH = 6

fun getDaysInMonth(month: Int, year: Int): Int {
    return when (month) {
        Calendar.JANUARY, Calendar.MARCH, Calendar.MAY, Calendar.JULY, Calendar.AUGUST, Calendar.OCTOBER, Calendar.DECEMBER -> 31
        Calendar.APRIL, Calendar.JUNE, Calendar.SEPTEMBER, Calendar.NOVEMBER -> 30
        Calendar.FEBRUARY -> if (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)) 29 else 28
        else -> throw IllegalArgumentException("Invalid Month")
    }
}


class DatePickerView @JvmOverloads constructor(
    context: Context,
    attributesSet: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStileRes: Int = 0
) : View(context, attributesSet, defStyleAttr, defStileRes) {


    val res = context.resources
    var monthData: CalendarData.Month = CalendarData.Month(2)
    private var dayOfWeekLabels = arrayOf("Пн", "Вт", "Cр", "Чт", "Пт", "Сб", "Вс")
    private var locale: Locale = Locale("ru", "RU")
    private var calendar = Calendar.getInstance(locale);

    private var mDesiredMonthHeight = res.getDimensionPixelSize(R.dimen.month_height)
    private var mDesiredDayOfWeekHeight = res.getDimensionPixelSize(R.dimen.day_of_week_height)
    private var mDesiredDayHeight = res.getDimensionPixelSize(R.dimen.day_height)
    private var mDesiredCellWidth = res.getDimensionPixelSize(R.dimen.day_width)
    private var mDesiredDaySelectorRadius = res.getDimensionPixelSize(R.dimen.day_selector_radius)

    private val monthPaint = TextPaint()
    private val dayOfWeekPaint = TextPaint()
    private val dayPaint = TextPaint()
    private val hoursPaint = TextPaint()
    private val workDayPaint = Paint()
    private val daySelectorPaint = Paint()
    private val dayHighlightPaint = Paint()
    private val dayHighlightSelectorPaint = Paint()

    private var monthYearLabel: String = ""

    private var month = calendar.get(Calendar.MONTH)
    private var year = calendar.get(Calendar.YEAR)


    private var mMonthHeight = 128
    private var mDayOfWeekHeight = 128
    private var mDayHeight = 128
    private var mCellWidth = 128
    private var mDaySelectorRadius = 60

    private var mPaddedWidth = 0
    private var mPaddedHeight = 0


    private var mActivatedDay = -1


    private var mToday = calendar.get(Calendar.DATE)

    private var mDaysInMonth = getDaysInMonth(month, year)
    private var startDayOfWeek = Calendar.MONDAY

    var onDayClickListener: OnDayClickListener? = null

    private var mHighlightedDay = -1
    private var mIsTouchHighlighted = false

    init {
        updateMonthYearLabel()
        initPaints()
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val preferredHeight =
            mDesiredDayHeight * MAX_WEEKS_IN_MONTH + mDesiredDayOfWeekHeight + mDesiredMonthHeight + paddingTop + paddingBottom
        val preferredWidth = mDesiredCellWidth * DAYS_IN_WEEK + paddingStart + paddingEnd
        val resolvedWidth = resolveSize(preferredWidth, widthMeasureSpec)
        val resolvedHeight = resolveSize(preferredHeight, heightMeasureSpec)
        setMeasuredDimension(resolvedWidth, resolvedHeight)
    }


    override fun onDraw(canvas: Canvas) {
//        canvas.translate(paddingLeft.toFloat(), paddingTop.toFloat())
        drawMonth(canvas)
        drawDaysOfWeek(canvas)
        drawDays(canvas)
//        canvas.translate(-paddingLeft.toFloat(), -paddingTop.toFloat())
    }

    private fun drawMonth(canvas: Canvas) {
        val x: Float = mPaddedWidth / 2f
        val lineHeight: Float = monthPaint.ascent() + monthPaint.descent()
        val y: Float = (mMonthHeight - lineHeight) / 2f
        canvas.drawText(monthYearLabel, x, y, monthPaint)
    }

    private fun drawDaysOfWeek(canvas: Canvas) {
        val paint: TextPaint = dayOfWeekPaint
        val headerHeight: Int = mMonthHeight
        val rowHeight: Int = mDayOfWeekHeight
        val colWidth: Int = mCellWidth

        val halfLineHeight = (paint.ascent() + paint.descent()) / 2f
        val rowCenter = headerHeight + rowHeight / 2
        for (col in 0 until DAYS_IN_WEEK) {
            val colCenter = colWidth * col + colWidth / 2
            val label: String = dayOfWeekLabels[col]
            canvas.drawText(label, colCenter.toFloat(), rowCenter - halfLineHeight, paint)
        }
    }

    private fun drawDays(canvas: Canvas) {
        val textPaint: TextPaint = dayPaint
        val headerHeight: Int = mMonthHeight + mDayOfWeekHeight
        val rowHeight: Int = mDayHeight
        val colWidth: Int = mCellWidth

        val halfLineHeight = (textPaint.ascent() + textPaint.descent()) / 2f
        var rowStart = headerHeight
        var rowCenter = headerHeight + rowHeight / 2
        var rowAngle = headerHeight + rowHeight / 4
        var day = 1
        var col: Int = findDayOffset()
        while (day <= mDaysInMonth) {
            monthData.days.forEach {
                if (it.number == day) {
                    val colRect = Rect(
                        (colWidth * col),
                        (rowStart),
                        (colWidth * col + colWidth),
                        (rowStart + rowHeight)
                    )
                    val alpha = 255 / 12 * it.hours
                    if (alpha > 255) {
                        workDayPaint.alpha = 0x255
                    } else {
                        workDayPaint.alpha = alpha
                    }
                    canvas.drawRect(colRect, workDayPaint)
                    val colAngle = colWidth * col + colWidth / 4
                    canvas.drawText(
                        it.hours.toString(),
                        colAngle.toFloat(),
                        rowAngle.toFloat(),
                        hoursPaint
                    )
                }
            }

            val colCenter = colWidth * col + colWidth / 2
            val isDayActivated = mActivatedDay == day
            val isDayHighlighted = mHighlightedDay == day
            if (isDayActivated) {
                val paint: Paint =
                    if (isDayHighlighted) dayHighlightSelectorPaint else daySelectorPaint
                canvas.drawCircle(
                    colCenter.toFloat(),
                    rowCenter.toFloat(),
                    mDaySelectorRadius.toFloat(),
                    paint
                )
            } else if (isDayHighlighted) {
                canvas.drawCircle(
                    colCenter.toFloat(), rowCenter.toFloat(),
                    mDaySelectorRadius.toFloat(), dayHighlightPaint
                )
            }
            val isDayToday = mToday == day
            val dayTextColor: Int = if (isDayToday && !isDayActivated) {
                daySelectorPaint.color
            } else {
                res.getColor(R.color.day_text_color, context.theme)
            }
            textPaint.color = dayTextColor
            canvas.drawText(
                day.toString(),
                colCenter.toFloat(),
                rowCenter - halfLineHeight,
                textPaint
            )
            col++
            if (col == DAYS_IN_WEEK) {
                col = 0
                rowCenter += rowHeight
                rowStart += rowHeight
                rowAngle += rowHeight
            }
            day++
        }
    }


    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        if (!changed) {
            return
        }
        val w = right - left
        val h = bottom - top
        val paddingLeft = paddingLeft
        val paddingTop = paddingTop
        val paddingRight = paddingRight
        val paddingBottom = paddingBottom
        val paddedRight = w - paddingRight
        val paddedBottom = h - paddingBottom
        val paddedWidth = paddedRight - paddingLeft
        val paddedHeight = paddedBottom - paddingTop
        if (paddedWidth == mPaddedWidth || paddedHeight == mPaddedHeight) {
            return
        }
        mPaddedWidth = paddedWidth
        mPaddedHeight = paddedHeight

        // We may have been laid out smaller than our preferred size. If so,
        // scale all dimensions to fit.
        val measuredPaddedHeight = measuredHeight - paddingTop - paddingBottom
        val scaleH = paddedHeight / measuredPaddedHeight.toFloat()
        val monthHeight = (mDesiredMonthHeight * scaleH).toInt()
        val cellWidth = mPaddedWidth / DAYS_IN_WEEK
        mMonthHeight = monthHeight
        mDayOfWeekHeight = (mDesiredDayOfWeekHeight * scaleH).toInt()
        mDayHeight = (mDesiredDayHeight * scaleH).toInt()
        mCellWidth = cellWidth

        val maxSelectorWidth = cellWidth / 2 + Math.min(paddingLeft, paddingRight)
        val maxSelectorHeight = mDayHeight / 2 + paddingBottom
        mDaySelectorRadius = min(
            mDesiredDaySelectorRadius, min(maxSelectorWidth, maxSelectorHeight)
        )
    }


    private fun initPaints() {

        val monthTextSize = res.getDimensionPixelSize(R.dimen.month_text_size)
        val dayOfWeekTextSize = res.getDimensionPixelSize(R.dimen.week_text_size)
        val dayTextSize = res.getDimensionPixelSize(R.dimen.day_text_size)
        val hoursTextSize = res.getDimensionPixelSize(R.dimen.hours_text_size)

        monthPaint.isAntiAlias = true
        monthPaint.textSize = monthTextSize.toFloat()
        monthPaint.textAlign = Align.CENTER
        monthPaint.style = Paint.Style.FILL
        dayOfWeekPaint.isAntiAlias = true
        dayOfWeekPaint.textSize = dayOfWeekTextSize.toFloat()
        dayOfWeekPaint.textAlign = Align.CENTER
        dayOfWeekPaint.style = Paint.Style.FILL
        daySelectorPaint.isAntiAlias = true
        daySelectorPaint.style = Paint.Style.FILL
        daySelectorPaint.color = res.getColor(R.color.day_select_color, context.theme)
        dayHighlightPaint.isAntiAlias = true
        dayHighlightPaint.style = Paint.Style.FILL
        workDayPaint.isAntiAlias = true
        workDayPaint.style = Paint.Style.FILL
        workDayPaint.color = Color.GREEN
        dayHighlightSelectorPaint.isAntiAlias = true
        dayHighlightSelectorPaint.style = Paint.Style.FILL
        dayPaint.isAntiAlias = true
        dayPaint.textSize = dayTextSize.toFloat()
        dayPaint.textAlign = Align.CENTER
        dayPaint.style = Paint.Style.FILL
        hoursPaint.isAntiAlias = true
        hoursPaint.textSize = hoursTextSize.toFloat()
        hoursPaint.textAlign = Align.CENTER
        hoursPaint.style = Paint.Style.FILL
    }

    private fun updateMonthYearLabel() {
        val format = DateFormat.getBestDateTimePattern(
            locale, "MMMMy"
        )
        val formatter = SimpleDateFormat(format, locale)
        formatter.setContext(DisplayContext.CAPITALIZATION_FOR_STANDALONE)
        monthYearLabel = formatter.format(calendar.time)
    }

    private fun isValidDayOfMonth(day: Int): Boolean {
        return day in 1..mDaysInMonth
    }

    private fun findDayOffset(): Int {
        val offset = startDayOfWeek - Calendar.MONDAY
        return if (startDayOfWeek < Calendar.MONDAY) {
            offset + DAYS_IN_WEEK
        } else offset
    }

    private fun getDayAtLocation(x: Int, y: Int): Int {
        val paddedX = x - paddingLeft
        if (paddedX < 0 || paddedX >= mPaddedWidth) {
            return -1
        }
        val headerHeight = mMonthHeight + mDayOfWeekHeight
        val paddedY = y - paddingTop
        if (paddedY < headerHeight || paddedY >= mPaddedHeight) {
            return -1
        }
        val row = (paddedY - headerHeight) / mDayHeight
        val col: Int = paddedX * DAYS_IN_WEEK / mPaddedWidth
        val index: Int = col + row * DAYS_IN_WEEK
        val day = index + 1 - findDayOffset()
        return if (!isValidDayOfMonth(day)) {
            -1
        } else day
    }

    private fun onDayClicked(day: Int): Boolean {
        if (!isValidDayOfMonth(day)) {
            return false
        }
        mActivatedDay = day
        if (onDayClickListener != null) {
            val date = Calendar.getInstance()
            date[year, month] = day
            onDayClickListener?.onDayClick(this, date)
        }
        return true
    }


    private fun sameDay(day: Int, today: Calendar): Boolean {
        return year == today[Calendar.YEAR] && month == today[Calendar.MONTH] && day == today[Calendar.DAY_OF_MONTH]
    }

    fun setSelectedDay(dayOfMonth: Int) {
        mActivatedDay = dayOfMonth
        invalidate()
    }

    fun setMonthAndYear(month: Int, year: Int, data: CalendarData.Month) {
        this.month = month
        this.year = year
        monthData = data
        calendar[Calendar.MONTH] = this.month
        calendar[Calendar.YEAR] = this.year
        calendar[Calendar.DAY_OF_MONTH] = 1
        startDayOfWeek = calendar[Calendar.DAY_OF_WEEK]
        val today = Calendar.getInstance()
        mToday = -1
        mDaysInMonth = getDaysInMonth(this.month, this.year)
        for (i in 0 until mDaysInMonth) {
            val day = i + 1
            if (sameDay(day, today)) {
                mToday = day
            }
        }
        updateMonthYearLabel()
        invalidate()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = (event.x + 0.5f).toInt()
        val y = (event.y + 0.5f).toInt()
        when (val action = event.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                val touchedItem = getDayAtLocation(x, y)
                mIsTouchHighlighted = true
                if (mHighlightedDay != touchedItem) {
                    mHighlightedDay = touchedItem
                    invalidate()
                }
                if (action == MotionEvent.ACTION_DOWN && touchedItem < 0) {
                    return false
                }
            }
            MotionEvent.ACTION_UP -> {
                val clickedDay = getDayAtLocation(x, y)
                onDayClicked(clickedDay)
                // Reset touched day on stream end.
                mHighlightedDay = -1
                mIsTouchHighlighted = false
                invalidate()
            }
            MotionEvent.ACTION_CANCEL -> {
                mHighlightedDay = -1
                mIsTouchHighlighted = false
                invalidate()
            }
        }
        return true
    }


    interface OnDayClickListener {
        fun onDayClick(view: DatePickerView?, day: Calendar)
    }
}