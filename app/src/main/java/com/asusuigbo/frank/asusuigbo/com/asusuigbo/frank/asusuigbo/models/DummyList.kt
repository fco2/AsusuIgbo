package com.asusuigbo.frank.asusuigbo.com.asusuigbo.frank.asusuigbo.models

class DummyList {

    companion object {
        var dataList: ArrayList<QuestionGroup> = ArrayList()

        fun getList(): ArrayList<QuestionGroup>{
            populateWithDummyData()
            return dataList
        }

        private fun populateWithDummyData(){
            //------------------ Question 1
            var list = populateOptions("A option", "B option", "C option", "D option")
            var qg = QuestionGroup("First Question?", list, "2",
                    SharedData.SelectedAnswerIndex.toString())
            dataList.add(qg)

            //------------------ Question 2
            list = populateOptions("E option", "F option", "G option", "H option")
            qg = QuestionGroup("Next Question?", list, "3",
                    SharedData.SelectedAnswerIndex.toString())
            dataList.add(qg)

            //------------------ Question 3
            list = populateOptions("T option", "U option", "V option", "ver ver long option to " +
                    "see how wrapping works")
            qg = QuestionGroup("Semi last Question?", list, "1",
                    SharedData.SelectedAnswerIndex.toString())
            dataList.add(qg)

            //------------------ Question 3
            list = populateOptions("P option", "Q option", "R option", "S option")
            qg = QuestionGroup("Last Question?", list, "1",
                    SharedData.SelectedAnswerIndex.toString())
            dataList.add(qg)
        }

        private fun populateOptions(op1: String, op2: String, op3: String, op4: String): ArrayList<Option>{

            var tempList = ArrayList<Option>()
            val a = Option( op1)
            val b = Option( op2)
            val c = Option( op3)
            val d = Option( op4)
            tempList.add(a)
            tempList.add(b)
            tempList.add(c)
            tempList.add(d)
            return tempList
        }
    }
}