package aum.application.project.formassignment.models

data class Field(
    val isRequiredField: Boolean,
    val options: List<Option>,
    val title: String,
    val type: String
)