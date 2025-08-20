package com.kkw.pawket.ai.service

import org.springframework.stereotype.Component
import com.kkw.pawket.domain.board.BoardService
import com.kkw.pawket.domain.clinic.ClinicService
import com.kkw.pawket.domain.schedule.ScheduleService

@Component
class ToolRegistry(
    private val clinicService: ClinicService,
    private val boardService: BoardService,
    private val scheduleService: ScheduleService
) {
    fun invoke(name: String, args: Map<String, Any>): String = when (name) {
        "findVetClinics" -> clinicService.search(args["city"] as String).toJson()
        "vaccineSchedule" -> scheduleService.vaccine(
            args["petType"] as String, args["birthdate"] as String
        ).toJson()
        "localBoardSearch" -> boardService.search(
            args["boardType"] as String, args["q"] as String
        ).toJson()
        "bookReminder" -> scheduleService.remind(
            (args["petId"] as Number).toLong(), args["title"] as String, args["at"] as String
        ).toJson()
        else -> """{"error":"unknown_tool"}"""
    }
}