plugins {
    id("ivy.feature")
}
android {
    namespace = "com.ivy.timezone"
}


dependencies {
    implementation(projects.shared.base)
    implementation(projects.shared.data.core)
    implementation(projects.shared.domain)
    implementation(projects.shared.ui.core)
    implementation(projects.shared.ui.navigation)
    implementation(projects.temp.legacyCode)
    implementation(projects.temp.oldDesign)
    implementation(projects.widget.balance)
    implementation(libs.androidx.core.i18n)

    testImplementation(projects.shared.ui.testing)
}
