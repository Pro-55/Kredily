package com.example.kredily.model

data class HomeScreenState(
    val searchQuery: String = "",
    val filterOptions: List<FilterOption> = listOf(),
    val filters: List<Filter> = listOf(),
    val employees: List<Employee> = listOf(),
    val filteredEmployees: List<Employee> = listOf()
)