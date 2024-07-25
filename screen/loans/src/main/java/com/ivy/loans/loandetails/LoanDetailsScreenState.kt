package com.ivy.loans.loandetails

import com.ivy.legacy.datamodel.Account
import com.ivy.legacy.datamodel.Loan
import com.ivy.legacy.domain.data.IvyTimeZone
import com.ivy.loans.loan.data.DisplayLoanRecord
import com.ivy.wallet.ui.theme.modal.LoanModalData
import com.ivy.wallet.ui.theme.modal.LoanRecordModalData
import kotlinx.collections.immutable.ImmutableList

data class LoanDetailsScreenState(
    val timeZone:IvyTimeZone?,
    val baseCurrency: String,
    val loan: Loan?,
    val displayLoanRecords: ImmutableList<DisplayLoanRecord>,
    val loanTotalAmount: Double,
    val amountPaid: Double,
    val loanAmountPaid: Double,
    val accounts: ImmutableList<Account>,
    val selectedLoanAccount: Account?,
    val createLoanTransaction: Boolean,
    val loanModalData: LoanModalData?,
    val loanRecordModalData: LoanRecordModalData?,
    val waitModalVisible: Boolean,
    val isDeleteModalVisible: Boolean
)
