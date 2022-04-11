package com.fanrong.frwallet.tools;

import org.bitcoinj.crypto.ChildNumber;
import org.bitcoinj.crypto.DeterministicHierarchy;
import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.crypto.HDKeyDerivation;
import org.bitcoinj.crypto.HDUtils;

import java.util.List;

import static org.web3j.crypto.MnemonicUtils.generateSeed;

public class bip44Utils {
    public static byte[] importAuxiliaries(String menmory,String Path){
        byte[] seed = generateSeed(menmory, null);
        DeterministicKey rootPrivateKey = HDKeyDerivation.createMasterPrivateKey(seed);
        // 4. 由根私钥生成 第一个HD 钱包
        DeterministicHierarchy dh = new DeterministicHierarchy(rootPrivateKey);
        // 5. 定义父路径 H则是加强 imtoken中的eth钱包进过测试发现使用的是此方式生成 bip44
        Path = Path.replace("’","H");
        Path = Path.replace("'","H");
        List<ChildNumber> parentPath = HDUtils.parsePath(Path.toUpperCase());  //m/44'/60'/0'/0 需要转换成 M/44H/60H/0H/0
        // 6. 由父路径,派生出第一个子私钥 "new ChildNumber(0)" 表示第一个 （m/44'/60'/0'/0/0）
//        DeterministicKey child = dh.deriveChild(parentPath, true, true, new ChildNumber(0)); //   m/44'/60'/0'/0/0
        DeterministicKey rootKey = dh.get(parentPath,true,true); // m/44'/60'/0'/0
        byte[] privateKeyByte = rootKey.getPrivKeyBytes();
        return privateKeyByte;
    }
}
