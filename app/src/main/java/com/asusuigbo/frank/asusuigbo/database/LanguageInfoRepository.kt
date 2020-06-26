package com.asusuigbo.frank.asusuigbo.database

class LanguageInfoRepository(private val userId: String, private val dao: LanguageInfoDao) {
    suspend fun getActiveLanguage(): LanguageInfo?{
        return dao.getActiveLanguage(userId)
    }

    suspend fun addLanguage(languageInfo: LanguageInfo){
        dao.addLanguage(languageInfo)
    }

    suspend fun getLanguage(language: String): LanguageInfo?{
        return dao.getLanguage(userId, language)
    }

    suspend fun update(notActive: Boolean){
        dao.update(userId, notActive)
    }

    suspend fun delete(){
        dao.delete()
    }
}