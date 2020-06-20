package com.asusuigbo.frank.asusuigbo.database

class LanguageInfoRepository(private val dao: LanguageInfoDao) {
    suspend fun getActiveLanguage(): LanguageInfo?{
        return dao.getActiveLanguage()
    }

    suspend fun addLanguage(languageInfo: LanguageInfo){
        dao.addLanguage(languageInfo)
    }

    suspend fun getLanguage(language: String): LanguageInfo?{
        return dao.getLanguage(language)
    }

    suspend fun update(not_active: Boolean){
        dao.update(not_active)
    }

    suspend fun delete(){
        dao.delete()
    }
}