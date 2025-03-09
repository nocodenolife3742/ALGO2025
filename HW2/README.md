# HW2

## bonus work

寫了一個 DP (dynamic programming) 的解法，比 HashTable 版本快了四倍左右

## apologize

我的ijava環境架不起來，他大部分運行的時候是好的，但遇到`assert`相關的程式時他會出一些小狀況(我有加`-ea`到args裡)，例如：

### problem : assert 永遠都是 true
- code : HW2 test2
- output : 輸出不對，但`assert`依舊通過
``` txt
Test of the method count(int n) of CountConfigurationsNaive for n=
    Calculating the number of grids of size 0x0 ... 1 (Time of calculating : 0.89 ms)
[OK]
    Calculating the number of grids of size 1x1 ... 3 (Time of calculating : 0.00 ms)
[OK]
    Calculating the number of grids of size 2x2 ... 9 (Time of calculating : 1.39 ms)
[OK]
    Calculating the number of grids of size 3x3 ... 27 (Time of calculating : 0.12 ms)
[OK]
```

### problem : assert 在某些情況會卡住線程
- code : HW1 Test21b
- output : 無輸出，因為他卡了5分鐘以上，用原專案跑不到1秒

由於以上原因，這次作業還是會用.java繳交，若未來環境修好，會改用.ipynb繳交，造成批改者的困擾非常抱歉 m(>︵< m)