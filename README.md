# escpos

特点:
escpos 是一个xml模版转化成热敏打印指令的库,本项目适用于java和android
轻量级 jar包只有75k大小
转换效率贼高,无强制任何依赖

[jar包下载地址](https://github.com/housong12590/escpos/blob/master/escpos.jar)


[xsd文件下载地址](https://github.com/housong12590/escpos/blob/master/print_template.xsd)

[示例模版](https://github.com/housong12590/escpos/blob/master/example.xml)

依赖事项 :
escpos 需要使用到json解析库,  支持 gson,jackson,fastjson进行解析, 如果你的项目里面有这些组件,escpos会自动选择合适的库解析
**注: 如果没有以上的json解析库需要自己添加依赖,否则无法使用**
**注: 如果有使用二维码和条形码功能需要自己添加com.google.zxing的依赖库**

使用方法:
escpos默认实现了网络打印的功能,使用如下 
```java
NetworkPrinter printer = new NetworkPrinter("192.168.10.99"); // 默认9100端口,可以指定端口和socket连接超时时间
printer.alwaysKeepConnect(true); // 是否一直保持跟打印机的连接
printer.setConnectKeepTime(60 * 1000); // 最后一个打印任务完成后,跟打印机保持连接的时间 , 默认是任务队列打印完成之后就断开跟打印机的连接, 如果打印任务很频繁 建议使用alwaysKeepConnect , 如果打印分高峰低峰建议设置setConnectKeepTime 减小每次都跟打印机进行连接的消耗 . alwaysKeepConnect的优先级高于setConnectKeepTime
printer.setOnPrintCallback(new OnPrintCallback());//设置打印任务的回调
printer.print("xml模版内容",data); // 该方法会返回本次打印任务的id,最后会在OnPrintCallback中回调, 打印ID可以自己设置
// data参数是在模版中使用的数据 , 类型是Map, map内不应该有实体类对象 , 如果是自定义对象 使用JSONUtil.toMap(Object);
```

模版变量
xml模版文件里,可以使用`${key}`,`#{key}`这两种表达式 ,表达式支持级联属性 ,即`${keys.name}`或`#{data.user.googds}`
`${key}` $表达式是在模版加载时替换
`#{key}` #表达式是在模版解析时替换 , 一般用于需要循环的标签
注: 不支持`${data.users[0]}`此种从数组中取值 的表达式
xml支持以下的元素

**condition** 属性用于判断是否满足条件 , 如果不满足条件, 就不生成该标签即标签内的所有元素 , 该属性适用于 image,text,section,group,table
判断规则
可以是字面量 true | false 
也可是一data里的某一个bool属性的值,如`#{data.isShow}`
也可以用于判某个属性是否存在 , 如果存在返回true, 不存在返回false 
```
<text value="买家备注: #{data.remark}" condition="#{data.remark}"/> //data里面有remark就生成当前text标签 ,没有就不生成 
```

### group
group 标签主要用于循环 ,内层可以嵌套任何标签

属性

- repeatKey 需要遍历的数组

### text 
一个文本标签 , 一个text标签结束后会换到下一行重新开始
    属性
    
- value 文本内容
- bold 文字加粗
- size 文字大小 normal,big,oversized 或者 x1,x2,x3
- align 文字对齐方向 left,center,right
- underline 下划线
- repeat 重复value值的内容 
    - none 不重复
    - fill 填充至行尾
    - auto 如果text标签单独使用效果类似fill填充至行尾 , 如果text标签放在section内,则填充行内剩余的空间
- marginLeft 左边空多少个字符
- marginRight 右边空多少个字符
- marginTop 上边空多少行
- marginBottom 下边空多少行
- margin 使用示例

    ```xml
    margin="5" // 左上右下
    margin="5,5" // 左右
    margin="5,5,5" // 左上右
    margin="5,5,5,5" //左上右下
    ```
    text使用示例
    ```
     <text value="${keys.title}" bold="true" align="center" size="big"/>
    ```
### section 
一个段落标签 , 里面可以放的元素只有text ,默认text标签结束会换到下一行 , 但是text被包裹到section标签内,则不会换行, section标签可以放多个text标签
属性

- marginTop 同text属性
- marginBottom 同text属性
示例:
```
  <section>
        <text value="台 号: ${keys.table_name}"/>
        <text value=" " repeat="auto"/>
        <text value="人  数: ${keys.guests}"/>
    </section>
```
### image
属性

- type image标签的类型
    - image 图片类型 
    - qrcode 二维码
    - barcode 条形码
- value 如果是type是qrcode或者barcode 填写需要生成的值 , 如果是image类型 value可以填写文件地址或者图片的url都可以  **(必填)**
- width 图片的宽  **(必填)**
- height 图片的高  **(必填)**
- align 图片的对齐方式 left,center,right
使用示例
```
<image type="qrcode" value="1000111" width="80" align="center" height="80"/>
<image type="image" value="C:\Users\Administrator\Desktop\timg.jpg" width="80" align="center" height="80"/>
```
### table 
table用于输出如商品列表,消费明细等
可以在`tr`标签上repeatKey 指定`repeat`属性为true,table标签会根据`repeatKey`的值进行遍历 ,重复当前的`tr`标签
使用 weight 来指定当前的td标签在行内占用多少比例的宽度
使用示例:
```xml
 <table>
        <tr bold="true"> 
            <td value="商品名称" weight="5"/>
            <td value="数量" weight="1" align="center"/>
            <td value="价格" weight="1" align="right"/>
        </tr>
        <tr repeat="true" repeatKey="#{goods}">
            <td weight="5" value="#{name}"/>
            <td weight="1" value="#{num}" align="center"/>
            <td weight="1" value="#{price}" align="right"/>
        </tr>
    </table>
```










