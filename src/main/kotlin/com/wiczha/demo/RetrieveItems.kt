package com.wiczha.demo

import aws.sdk.kotlin.services.rdsdata.RdsDataClient
import aws.sdk.kotlin.services.rdsdata.model.ExecuteStatementRequest
import aws.sdk.kotlin.services.rdsdata.model.Field
import org.springframework.stereotype.Component

@Component
class RetrieveItems {

    private val secretArnVal : String = System.getenv("SECRET_ARN_VAL")
    private val resourceArnVal : String = System.getenv("RESOURCE_ARN_VAL")


    // Get items from the database.
    suspend fun getItemsDataSQL(): MutableList<UserItem> {
        val records = mutableListOf<UserItem>()
        val sqlStatement = "select UserId FROM testDB.users"
        val sqlRequest = ExecuteStatementRequest {
            secretArn = secretArnVal
            sql = sqlStatement
            database = "testDB"
            resourceArn = resourceArnVal
        }
        RdsDataClient { region = "eu-west-1" }.use { rdsDataClient ->
            val response = rdsDataClient.executeStatement(sqlRequest)
            val dataList: List<List<Field>>? = response.records
            var userItem: UserItem
            var index: Int

            if (dataList != null) {
                for (list in dataList) {
                    userItem = UserItem()
                    index = 0
                    for (iteratedField in list) {
                        val field: Field = iteratedField
                        val result = field.toString()
                        val value = result.substringAfter("=").substringBefore(')')
                        if (index == 0) {
                            userItem.id = value
                        } else if (index == 1) {
                            userItem.date = value
                        } else if (index == 2) {
                            userItem.description = value
                        } else if (index == 3) {
                            userItem.quiz = value
                        } else if (index == 4) {
                            userItem.status = value
                        } else if (index == 5) {
                            userItem.name = value
                        }
                        index++
                    }
                    records.add(userItem)
                }
            }
        }
        print(records)
        return records
    }

}