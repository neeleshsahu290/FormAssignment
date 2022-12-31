package aum.application.project.formassignment.models

data class Option(
    val itemId: String,
    val itemName: String

){
    override fun toString(): String {
        return itemName
    }
}