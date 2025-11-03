package com.example.workerapp.utils

object JobStatus {
    val HIRING = "Hiring"
    val NOT_PAYMENT = "Not_payment"
    val COMPLETED = "Completed"
    val CANCEL = "Cancel"
    val PROCESSING = "processing"
    val WAITING = "Waiting"
    val ACCEPTED = "Accepted"

    fun getLabel(status: String) : String{
        when (status){
            HIRING -> return "Đang tuyển dụng"
            NOT_PAYMENT -> return "Chưa thanh toán"
            COMPLETED -> return "Hoàn thành"
            CANCEL -> return "Đã hủy"
            PROCESSING -> return "Đang xử lý"
            WAITING -> return "Đang chờ"
            ACCEPTED -> return "Đã chấp nhận"
            else -> return "Không xác định"
        }
    }
}

