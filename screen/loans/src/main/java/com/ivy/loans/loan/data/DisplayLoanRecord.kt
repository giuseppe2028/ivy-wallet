package com.ivy.loans.loan.data

import com.ivy.legacy.datamodel.Account
import com.ivy.legacy.datamodel.LoanRecord
import com.ivy.legacy.domain.data.IvyTimeZone

data class DisplayLoanRecord(
    val loanRecord: LoanRecord,
    val account: Account? = null,
    val loanRecordCurrencyCode: String = "",
    val timeZone: IvyTimeZone? = null,
    val loanCurrencyCode: String = "",
    val loanRecordTransaction: Boolean = false,
)
