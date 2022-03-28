package com.fanrong.frwallet.dao.database

import org.litepal.LitePal
import xc.common.tool.utils.checkNotEmpty

object AddressModelOperator {

    fun saveAddr(addressModel: AddressDao): Boolean {
        val find = LitePal.where("uniqueKey = ?", addressModel.type + "_" + addressModel.address)
            .find(AddressDao::class.java)

        if (find.checkNotEmpty()) {
            return false
        }
        addressModel.uniqueKey = addressModel.type + "_" + addressModel.address

        return addressModel.save()
    }

    fun getAllAddr(): MutableList<AddressDao>? {
        return LitePal.findAll(AddressDao::class.java)
    }

    fun getAllAddrByType(type: String): MutableList<AddressDao>? {
        return LitePal.where("type = ?", type).find(AddressDao::class.java)
    }


    fun delete(addressModel: AddressDao) {
        LitePal.deleteAll(AddressDao::class.java, "uniqueKey = ?", addressModel.type + "_" + addressModel.address)
    }


    /**
     * 判断新数据是否存在，不存在再删除就数据，保存为新的一条数据，
     */
    fun upadate(addressModel: AddressDao): Boolean {
        val unique = addressModel.type + "_" + addressModel.address


        val find = LitePal.where("uniqueKey = ?", unique)
            .find(AddressDao::class.java)

        if (find.checkNotEmpty() && !unique.equals(addressModel.uniqueKey)) {
            return false
        }
        LitePal.deleteAll(AddressDao::class.java, "uniqueKey = ?", addressModel.uniqueKey)

        return saveAddr(addressModel)
//        LitePal.deleteAll(AddressModel::class.java, "uniqueKey = ?", addressModel.type + "_" + addressModel.address)
    }

}