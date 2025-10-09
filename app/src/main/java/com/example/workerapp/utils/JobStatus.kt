package com.example.workerapp.utils

object JobStatus {
    val HIRING = "Hiring"
    val NOT_PAYMENT = "not_payment"
    val COMPLETE = "complete"
    val CANCEL = "cancel"
    val PROCESSING = "processing"

    fun getLabel(status: String) : String{
        when (status){
            HIRING -> return "Đang tuyển dụng"
            NOT_PAYMENT -> return "Chưa thanh toán"
            COMPLETE -> return "Hoàn thành"
            CANCEL -> return "Đã hủy"
            PROCESSING -> return "Đang xử lý"
            else -> return "Không xác định"
        }
    }
}

