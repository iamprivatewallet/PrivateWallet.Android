package com.fanrong.frwallet.inteface;

import com.fanrong.frwallet.dao.database.WalletDao;

public interface OnSelectWalletDialogItemClick {
    public void selectWallet(WalletDao walletDao);
}
