package com.ivy.loans.loan.data

import com.ivy.legacy.datamodel.Account
import com.ivy.legacy.datamodel.LoanRecord
import com.ivy.legacy.domain.data.IvyTimeZone

data class DisplayLoanRecord(
    val loanRecord: LoanRecord,
    val timeZone: IvyTimeZone,
    val account: Account? = null,
    val loanRecordCurrencyCode: String = "",
    val loanCurrencyCode: String = "",
    val loanRecordTransaction: Boolean = false,
)
