# CommentDialog
这是一个Android评论框，类似抖音评论弹框

![在这里插入图片描述](https://img-blog.csdnimg.cn/20191223183926488.jpg)
![在这里插入图片描述](https://img-blog.csdnimg.cn/20191223183944984.jpg)
![在这里插入图片描述](https://img-blog.csdnimg.cn/20191223184007906.jpg)


最近公司有个需求，就是仿抖音弹窗评论，我也是在网上找了一些资料，
并未找到有人实现了此功能，于是我就根据一些资料自己去实现。

这个弹窗可以用 Design 中的BottomSheetDialog来实现，可以完全把它当做一个dialog来做，
在dialog基础上又增加了下拉回弹 下拉关闭效果，
不过我对这个也不太熟，仅仅在此使用了一次，所以对这个就不过多的说明了。
整个功能包括：添加一级评论 添加二级评论 点赞 回复二级评论

## 具体实现
外框是BottomSheetDialog，布局主要是一个Recyclerview，由于二级评论的原因，我用了2种方式去实现，主要是数据的区别

1、整个列表使用Recyclerview：一级评论为Item 二级评论使用LinearLayout来动态增加，其中回复的二级评论使用富文本SpannableString，可点击
数据格式List<一级评论bean>， 一级评论的实体类中包含了二级评论列表 
在adapter中把二级评论List传入自定义的Linearlayout 让其动态添加view

未解决问题：由于动态添加的view过多时 会使得相应的Item高度过长 甚至超过整个屏幕的高度 导致滑动有些卡顿
           也许是加载数据方式不对，很多地方需要优化导致的卡顿 由于我经验尚不足，未发现具体原因
           有发现原因的欢迎探讨指教

2、整个列表使用Recyclerview：一级评论、二级评论、展示更多均为Item adapter根据类型去加载相应的item
也就是说任何一条评论都为一个item 根据普通的多类型列表去加载即可 
暂未发现问题 滑动也比较流畅等 

就是一些细节处理较为麻烦：比如添加了一条评论 如何滑动到相应的位置等

## 参考
[Android评论回复弹框](https://blog.csdn.net/qq_32518491/article/details/85000421)

[解决BottomSheetDialog中的RecyclerView要点击两次才能触发点击事件的问题](https://blog.csdn.net/qq_25138543/article/details/105733221)


### about
如果我的代码对你有帮助，请给我一个star
