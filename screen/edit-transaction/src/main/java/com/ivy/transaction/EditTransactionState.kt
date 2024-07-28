package com.ivy.transaction

import androidx.compose.runtime.Immutable
import com.ivy.base.model.TransactionType
import com.ivy.data.model.Category
import com.ivy.data.model.Tag
import com.ivy.data.model.TagId
import com.ivy.legacy.data.EditTransactionDisplayLoan
import com.ivy.legacy.datamodel.Account
import com.ivy.legacy.domain.data.IvyTimeZone
import com.ivy.wallet.domain.data.CustomExchangeRateState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableSet
import java.time.Instant

@Immutable
data class EditTransactionState(
    val transactionType: TransactionType,
    val initialTitle: String?,
    val titleSuggestions: ImmutableSet<String>,
    val currency: String,
    val timeZone: IvyTimeZone,
    val description: String?,
    val dateTime: Instant?,
    val dueDate: Instant?,
    val accounts: ImmutableList<Account>,
    val categories: ImmutableList<Category>,
    val account: Account?,
    val toAccount: Account?,
    val category: Category?,
    val amount: Double,
    val hasChanges: Boolean,
    val displayLoanHelper: EditTransactionDisplayLoan,
    val backgroundProcessingStarted: Boolean,
    val customExchangeRateState: CustomExchangeRateState,
    val tags: ImmutableList<Tag>,
    val transactionAssociatedTags: ImmutableList<TagId>
)
