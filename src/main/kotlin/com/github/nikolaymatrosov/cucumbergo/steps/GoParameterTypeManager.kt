package com.github.nikolaymatrosov.cucumbergo.steps


import com.intellij.psi.PsiElement
import com.intellij.psi.SmartPsiElementPointer
import org.jetbrains.plugins.cucumber.CucumberUtil
import org.jetbrains.plugins.cucumber.ParameterTypeManager

object GoParameterTypeManager : ParameterTypeManager {

    private const val defaultIntegerPattern = "-?\\d+"
    private const val defaultDecimalPattern = "-?\\d*[.,]?\\d+"
    private val defaultJvmParameterTypes = mapOf(
        "biginteger" to defaultIntegerPattern,
        "bigdecimal" to defaultDecimalPattern,
        "byte" to defaultIntegerPattern,
        "short" to defaultIntegerPattern,
        "long" to defaultIntegerPattern,
        "double" to defaultDecimalPattern
    )
    private val nameToParameterTypeMap = mutableMapOf<String, String>()
    private val nameToDeclarationMap = mutableMapOf<String, SmartPsiElementPointer<PsiElement>>()

    init {
        nameToParameterTypeMap.putAll(CucumberUtil.STANDARD_PARAMETER_TYPES)
        nameToParameterTypeMap.putAll(defaultJvmParameterTypes)
    }

    fun addParameterType(name: String, parameterType: String, declaration: SmartPsiElementPointer<PsiElement>) {
        synchronized(this) {
            nameToParameterTypeMap[name] = parameterType
            nameToDeclarationMap[name] = declaration
        }
    }

    override fun getParameterTypeValue(name: String): String? {
        synchronized(this) {
            return nameToParameterTypeMap[name]
        }
    }

    override fun getParameterTypeDeclaration(name: String): PsiElement? {
        synchronized(this) {
            return nameToDeclarationMap[name]?.element
        }
    }
}