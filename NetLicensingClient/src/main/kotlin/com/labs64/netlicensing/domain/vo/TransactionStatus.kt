package com.labs64.netlicensing.domain.vo

enum class TransactionStatus {

    /**
     * Transaction still running.
     */
    PENDING,

    /**
     * Transaction is closed.
     */
    CLOSED,

    /**
     * Transaction is cancelled.
     */
    CANCELLED
}
