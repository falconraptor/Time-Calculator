package com.falconraptor.birthdayCalculator

import java.awt.GridLayout
import java.awt.event.{ActionEvent, ActionListener}
import java.time.format.DateTimeFormatter
import java.time.{LocalDate, LocalDateTime, LocalTime}
import javax.swing._

object BirthdayCalculator {
  val window = new JFrame("Birthday Calculator")

  def main(args: Array[String]) {
    window setDefaultCloseOperation WindowConstants.EXIT_ON_CLOSE
    window setResizable false
    setGUI
    window setVisible true
  }

  def setGUI {
    val panel = new JPanel(new GridLayout(1, 4, 0, 0))
    val dateInput = new JFormattedTextField()
    val timeInput = new JFormattedTextField()
    val amInput = new JComboBox(Array("AM", "PM"))
    if (LocalTime.now().getHour > 12) amInput setSelectedIndex 1
    dateInput setValue LocalDate.now
    dateInput setFocusLostBehavior JFormattedTextField.COMMIT
    timeInput setValue LocalTime.now.format(DateTimeFormatter.ofPattern("h:m:s"))
    timeInput setFocusLostBehavior JFormattedTextField.COMMIT
    val calculate = new JButton("Calculate")
    panel add dateInput
    panel add timeInput
    panel add amInput
    panel add calculate
    val outputPanel = new JPanel(new GridLayout(2, 6, 0, 0))
    for (s <- Array("Years", "Months", "Days", "Hours", "Minutes", "Seconds")) outputPanel add new JLabel(s, SwingConstants CENTER)
    val years = new JLabel("", SwingConstants CENTER)
    val months = new JLabel("", SwingConstants CENTER)
    val days = new JLabel("", SwingConstants CENTER)
    val hours = new JLabel("", SwingConstants CENTER)
    val minutes = new JLabel("", SwingConstants CENTER)
    val seconds = new JLabel("", SwingConstants CENTER)
    calculate addActionListener new ActionListener() {
      override def actionPerformed(e: ActionEvent) {
        val now = LocalDateTime.now()
        dateInput commitEdit()
        timeInput commitEdit()
        val dateValue = dateInput getValue
        val timeValue = timeInput getValue
        val then = LocalDateTime parse dateValue + "T" + (if (amInput.getSelectedIndex == 0) timeValue else (timeValue.toString.split(":")(0).toInt + 12) + ":" + timeValue.toString.split(":")(1))
        var out: LocalDateTime = null
        if ((then compareTo now) > 0) out = subtract(then, now)
        else out = subtract(now, then)
        years setText out.getYear.toString
        months setText out.getMonthValue.toString
        days setText out.getDayOfMonth.toString
        hours setText out.getHour.toString
        minutes setText out.getMinute.toString
        seconds setText out.getSecond.toString
      }
    }
    outputPanel add years
    outputPanel add months
    outputPanel add days
    outputPanel add hours
    outputPanel add minutes
    outputPanel add seconds
    val main = new JPanel(new GridLayout(2, 1, 0, 0))
    main add panel
    main add outputPanel
    window setContentPane main
    window pack()
    window setLocationRelativeTo null
  }

  def subtract(then: LocalDateTime, now: LocalDateTime): LocalDateTime = {
    var out = then minusMinutes now.getMinute
    out = out minusHours now.getHour
    out = out minusDays now.getDayOfMonth
    out = out minusMonths now.getMonthValue
    out = out minusYears now.getYear
    out
  }
}
