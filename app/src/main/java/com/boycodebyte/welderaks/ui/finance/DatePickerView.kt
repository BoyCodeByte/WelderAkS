package com.boycodebyte.welderaks.ui.finance

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Paint.Align
import android.graphics.Rect
import android.icu.text.DateFormatSymbols
import android.icu.text.DisplayContext
import android.icu.text.RelativeDateTimeFormatter
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.text.TextPaint
import android.text.format.DateFormat
import android.util.AttributeSet
import android.view.View
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat
import androidx.customview.widget.ExploreByTouchHelper
import java.text.NumberFormat
import java.util.*
import kotlin.math.min

const val DAYS_IN_WEEK = 7
const val MAX_WEEKS_IN_MONTH = 6
const val DEFAULT_SELECTED_DAY = -1
const val DEFAULT_WEEK_START = Calendar.SUNDAY
const val MONTH_YEAR_FORMAT = "MMMMy"
const val DATE_FORMAT = "dd MMMM yyyy"
const val SELECTED_HIGHLIGHT_ALPHA = 0xB0

class DatePickerView @JvmOverloads constructor(
    context: Context,
    attributesSet: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStileRes: Int = 0
) : View(context, attributesSet, defStyleAttr, defStileRes) {


    val res = context.resources
    private var mDayOfWeekLabels = arrayOf("Пн", "Вт", "Cр", "Чт", "Пт", "Сб", "Вс")
    private var mLocale: Locale = res.configuration.locale
    private val mTouchHelper: MonthViewTouchHelper? = null
    private var mCalendar: Calendar = Calendar.getInstance(mLocale);
    private var mDayFormatter: NumberFormat = NumberFormat.getIntegerInstance(mLocale)


    //желаемая высота названия месяца
    private var mDesiredMonthHeight = 128

    //желаемая высота лэйблов дней недели
    private var mDesiredDayOfWeekHeight = 128

    //желаемая высота высоты дней
    private var mDesiredDayHeight = 128

    //желаемая ширина ячеек
    private var mDesiredCellWidth = 128
    private var mDesiredDaySelectorRadius = 64


    private val mMonthPaint = TextPaint()
    private val mDayOfWeekPaint = TextPaint()
    private val mDayPaint = TextPaint()
    private val mDaySelectorPaint = Paint()
    private val mDayHighlightPaint = Paint()
    private val mDayHighlightSelectorPaint = Paint()

    private var mMonthYearLabel: String = ""

    private var mMonth = 0
    private var mYear = 0

    // Dimensions as laid out.
    private var mMonthHeight = 128
    private var mDayOfWeekHeight = 128
    private var mDayHeight = 128
    private var mCellWidth = 128
    private var mDaySelectorRadius = 64

    private var mPaddedWidth = 0
    private var mPaddedHeight = 0


    private var mActivatedDay = 3


    private var mToday = DEFAULT_SELECTED_DAY


    private var mWeekStart = DEFAULT_WEEK_START

    private var mDaysInMonth = 28

    /**
     * The day of week (ex. Calendar.SUNDAY) for the first day of the current
     * month.
     */
    private var mDayOfWeekStart = 0

    private var mEnabledDayStart = 1
    private var mEnabledDayEnd = 31

    /** Optional listener for handling day click actions.  */
    private var mOnDayClickListener: OnDayClickListener? = null

    private var mDayTextColor: ColorStateList? = null

    private var mHighlightedDay = -1
    private var mPreviouslyHighlightedDay = -1
    private var mIsTouchHighlighted = false

    init {
        updateMonthYearLabel()
        updateDayOfWeekLabels()
        initPaints()
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val preferredHeight = mDesiredDayHeight * MAX_WEEKS_IN_MONTH
        +mDesiredDayOfWeekHeight + mDesiredMonthHeight
        +paddingTop + paddingBottom
        val preferredWidth = mDesiredCellWidth * DAYS_IN_WEEK + paddingStart + paddingEnd
        val resolvedWidth = resolveSize(preferredWidth, widthMeasureSpec)
        val resolvedHeight = resolveSize(preferredHeight, heightMeasureSpec)
        setMeasuredDimension(resolvedWidth, resolvedHeight)
    }


    override fun onDraw(canvas: Canvas) {
        canvas.translate(paddingLeft.toFloat(), paddingTop.toFloat())
        drawMonth(canvas)
        drawDaysOfWeek(canvas)
        drawDays(canvas)
        canvas.translate(-paddingLeft.toFloat(), -paddingTop.toFloat())
    }

    private fun drawMonth(canvas: Canvas) {
        val x: Float = mPaddedWidth / 2f
        val lineHeight: Float = mMonthPaint.ascent() + mMonthPaint.descent()
        val y: Float = (mMonthHeight - lineHeight) / 2f
        canvas.drawText(mMonthYearLabel, x, y, mMonthPaint)
    }

    private fun drawDaysOfWeek(canvas: Canvas) {
        val paint: TextPaint = mDayOfWeekPaint
        val headerHeight: Int = mMonthHeight
        val rowHeight: Int = mDayOfWeekHeight
        val colWidth: Int = mCellWidth

        // Text is vertically centered within the day of week height.
        val halfLineHeight = (paint.ascent() + paint.descent()) / 2f
        val rowCenter = headerHeight + rowHeight / 2
        for (col in 0 until DAYS_IN_WEEK) {
            val colCenter = colWidth * col + colWidth / 2
            val label: String = mDayOfWeekLabels[col]
            canvas.drawText(label, colCenter.toFloat(), rowCenter - halfLineHeight, paint)
        }
    }

    private fun drawDays(canvas: Canvas) {
        val textPaint: TextPaint = mDayPaint
        val headerHeight: Int = mMonthHeight + mDayOfWeekHeight
        val rowHeight: Int = mDayHeight
        val colWidth: Int = mCellWidth

        val halfLineHeight = (textPaint.ascent() + textPaint.descent()) / 2f
        var rowCenter = headerHeight + rowHeight / 2
        var day = 1
        var col: Int = findDayOffset()
        while (day <= mDaysInMonth) {
            val colCenter = colWidth * col + colWidth / 2
            var stateMask = 0
            val isDayEnabled: Boolean = isDayEnabled(day)
//            if (isDayEnabled) {
//                stateMask = stateMask or StateSet.VIEW_STATE_ENABLED
//            }
            val isDayActivated = mActivatedDay == day
            val isDayHighlighted = mHighlightedDay == day
            if (isDayActivated) {
//                stateMask = stateMask or StateSet.VIEW_STATE_ACTIVATED

                // Adjust the circle to be centered on the row.
                val paint: Paint =
                    if (isDayHighlighted) mDayHighlightSelectorPaint else mDaySelectorPaint
                canvas.drawCircle(
                    colCenter.toFloat(),
                    rowCenter.toFloat(),
                    mDaySelectorRadius.toFloat(),
                    paint
                )
            } else if (isDayHighlighted) {
//                stateMask = stateMask or StateSet.VIEW_STATE_PRESSED
                if (isDayEnabled) {

                    canvas.drawCircle(
                        colCenter.toFloat(), rowCenter.toFloat(),
                        mDaySelectorRadius.toFloat(), mDayHighlightPaint
                    )
                }
            }
            val isDayToday = mToday == day
            val dayTextColor: Int = if (isDayToday && !isDayActivated) {
                mDaySelectorPaint.color
            } else {
                Color.BLUE
            }
            textPaint.color = dayTextColor
            canvas.drawText(
                mDayFormatter.format(day.toLong()),

                colCenter.toFloat(),
                rowCenter - halfLineHeight,
                textPaint
            )
            col++
            if (col == DAYS_IN_WEEK) {
                col = 0
                rowCenter += rowHeight
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

        // Compute the largest day selector radius that's still within the clip
        // bounds and desired selector radius.
        val maxSelectorWidth = cellWidth / 2 + Math.min(paddingLeft, paddingRight)
        val maxSelectorHeight = mDayHeight / 2 + paddingBottom
        mDaySelectorRadius = min(
            mDesiredDaySelectorRadius, min(maxSelectorWidth, maxSelectorHeight)
        )
    }


    private fun initPaints() {

        val monthTextSize = 32

        val dayOfWeekTextSize = 32

        val dayTextSize = 64

        mMonthPaint.isAntiAlias = true
        mMonthPaint.textSize = monthTextSize.toFloat()
        mMonthPaint.textAlign = Align.CENTER
        mMonthPaint.style = Paint.Style.FILL
        mDayOfWeekPaint.isAntiAlias = true
        mDayOfWeekPaint.textSize = dayOfWeekTextSize.toFloat()
        mDayOfWeekPaint.textAlign = Align.CENTER
        mDayOfWeekPaint.style = Paint.Style.FILL
        mDaySelectorPaint.isAntiAlias = true
        mDaySelectorPaint.style = Paint.Style.FILL
        mDaySelectorPaint.color = Color.BLACK
        mDayHighlightPaint.isAntiAlias = true
        mDayHighlightPaint.style = Paint.Style.FILL
        mDayHighlightSelectorPaint.isAntiAlias = true
        mDayHighlightSelectorPaint.style = Paint.Style.FILL
        mDayPaint.isAntiAlias = true
        mDayPaint.textSize = dayTextSize.toFloat()
        mDayPaint.textAlign = Align.CENTER
        mDayPaint.style = Paint.Style.FILL
    }

    private fun updateMonthYearLabel() {
        val format = DateFormat.getBestDateTimePattern(
            mLocale, MONTH_YEAR_FORMAT
        )
        val formatter = SimpleDateFormat(format, mLocale)
        formatter.setContext(DisplayContext.CAPITALIZATION_FOR_STANDALONE)
        mMonthYearLabel = formatter.format(mCalendar.time)
    }

    private fun updateDayOfWeekLabels() {
        // Use tiny (e.g. single-character) weekday names from ICU. The indices
        // for this list correspond to Calendar days, e.g. SUNDAY is index 1.
        val tinyWeekdayNames = DateFormatSymbols.getInstance(mLocale)
            .getWeekdays(DateFormatSymbols.FORMAT, DateFormatSymbols.NARROW)
        for (i in 0 until DAYS_IN_WEEK) {
            mDayOfWeekLabels[i] =
                tinyWeekdayNames[(mWeekStart + i - 1) % DAYS_IN_WEEK + 1]
        }
    }


    private fun isDayEnabled(day: Int): Boolean {
        return day in mEnabledDayStart..mEnabledDayEnd
    }

    private fun isValidDayOfMonth(day: Int): Boolean {
        return day in 1..mDaysInMonth
    }

    private fun isValidDayOfWeek(day: Int): Boolean {
        return day >= Calendar.SUNDAY && day <= Calendar.SATURDAY
    }

    private fun isValidMonth(month: Int): Boolean {
        return month >= Calendar.JANUARY && month <= Calendar.DECEMBER
    }

    private fun findDayOffset(): Int {
        val offset = mDayOfWeekStart - mWeekStart
        return if (mDayOfWeekStart < mWeekStart) {
            offset + DAYS_IN_WEEK
        } else offset
    }

    fun getDayAtLocation(x: Int, y: Int): Int {
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

    /**
     * Calculates the bounds of the specified day.
     *
     * @param id the day of the month
     * @param outBounds the rect to populate with bounds
     */
    fun getBoundsForDay(id: Int, outBounds: Rect): Boolean {
        if (!isValidDayOfMonth(id)) {
            return false
        }
        val index = id - 1 + findDayOffset()

        // Compute left edge, taking into account RTL.
        val col: Int = index % DAYS_IN_WEEK
        val colWidth = mCellWidth
        val left: Int = paddingLeft + col * colWidth


        // Compute top edge.
        val row: Int = index / DAYS_IN_WEEK
        val rowHeight = mDayHeight
        val headerHeight = mMonthHeight + mDayOfWeekHeight
        val top = paddingTop + headerHeight + row * rowHeight
        outBounds[left, top, left + colWidth] = top + rowHeight
        return true
    }

    private fun onDayClicked(day: Int): Boolean {
        if (!isValidDayOfMonth(day) || !isDayEnabled(day)) {
            return false
        }
        if (mOnDayClickListener != null) {
            val date = Calendar.getInstance()
            date[mYear, mMonth] = day
            mOnDayClickListener!!.onDayClick(this, date)
        }

        // This is a no-op if accessibility is turned off.
        mTouchHelper?.sendEventForVirtualView(day, AccessibilityEvent.TYPE_VIEW_CLICKED)
        return true
    }

    fun setFirstDayOfWeek(weekStart: Int) {
        mWeekStart = if (isValidDayOfWeek(weekStart)) {
            weekStart
        } else {
            mCalendar.firstDayOfWeek
        }
        updateDayOfWeekLabels()
        // Invalidate cached accessibility information.
        mTouchHelper!!.invalidateRoot()
        invalidate()
    }





    interface OnDayClickListener {
        fun onDayClick(view: DatePickerView?, day: Calendar?)
    }


    inner class MonthViewTouchHelper(host: View) :
        ExploreByTouchHelper(host) {
        private val mTempRect = Rect()
        private val mTempCalendar = Calendar.getInstance()

        override fun getVirtualViewAt(x: Float, y: Float): Int {
            val day: Int = getDayAtLocation((x + 0.5f).toInt(), (y + 0.5f).toInt())
            return if (day != -1) {
                day
            } else ExploreByTouchHelper.INVALID_ID
        }

        override fun getVisibleVirtualViews(virtualViewIds: MutableList<Int>?) {
            for (day in 1..mDaysInMonth) {
                virtualViewIds?.add(day)
            }
        }

        override fun onPopulateEventForVirtualView(virtualViewId: Int, event: AccessibilityEvent) {
            event.contentDescription = getDayDescription(virtualViewId)
        }

        override fun onPopulateNodeForVirtualView(
            virtualViewId: Int,
            node: AccessibilityNodeInfoCompat
        ) {
            val hasBounds: Boolean = getBoundsForDay(virtualViewId, mTempRect)
            if (!hasBounds) {
                // The day is invalid, kill the node.
                mTempRect.setEmpty()
                node.contentDescription = ""
                node.setBoundsInParent(mTempRect)
                node.isVisibleToUser = false
                return
            }
            node.text = getDayText(virtualViewId)
            node.contentDescription = getDayDescription(virtualViewId)
            if (virtualViewId == mToday) {
                val fmt = RelativeDateTimeFormatter.getInstance()
                node.stateDescription = fmt.format(
                    RelativeDateTimeFormatter.Direction.THIS,
                    RelativeDateTimeFormatter.AbsoluteUnit.DAY
                )
            }
            if (virtualViewId == mActivatedDay) {
                node.isSelected = true
            }
            node.setBoundsInParent(mTempRect)
            val isDayEnabled: Boolean = isDayEnabled(virtualViewId)
            if (isDayEnabled) {
                node.addAction(AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_CLICK)
            }
            node.isEnabled = isDayEnabled
            node.isClickable = true
            if (virtualViewId == mActivatedDay) {
                // TODO: This should use activated once that's supported.
                node.isChecked = true
            }
        }

        override fun onPerformActionForVirtualView(
            virtualViewId: Int, action: Int,
            arguments: Bundle?
        ): Boolean {
            when (action) {
                AccessibilityNodeInfo.ACTION_CLICK -> return onDayClicked(virtualViewId)
            }
            return false
        }

        /**
         * Generates a description for a given virtual view.
         *
         * @param id the day to generate a description for
         * @return a description of the virtual view
         */
        private fun getDayDescription(id: Int): CharSequence {
            if (isValidDayOfMonth(id)) {
                mTempCalendar[mYear, mMonth] = id
                return DateFormat.format(DATE_FORMAT, mTempCalendar.timeInMillis)
            }
            return ""
        }

        /**
         * Generates displayed text for a given virtual view.
         *
         * @param id the day to generate text for
         * @return the visible text of the virtual view
         */
        private fun getDayText(id: Int): CharSequence? {
            return if (isValidDayOfMonth(id)) {
                mDayFormatter.format(id.toLong())
            } else null
        }
    }
}