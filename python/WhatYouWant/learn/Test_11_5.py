# 科学计算，服务器开发，web应用开发
# .py文件->

# 单行注释
'''
多行注释
'''

"""
这也是多行注释
"""
import keyword


def test1():
    age = 25
    if age >= 18:
        print("成年人")
    else:
        print("未成年")


def test2():
    i = 0
    while i < 1000:
        print(" i love you")
        i += 1

def test3():
    name = "julyedu.com"
    for i in name:
        print(i)
    else:
        print("end")

def test4():
    a,b,c,d=20,5.5,True,4+3j
    print(type(a))
    print(type(b))
    print(type(c))
    print(type(d))

def test5():
    namelist =['1','2','3']
    namelist.append('hello') #进行追加
    # extend
    a=[1,2]
    b=[3,4]
    a.extend(b) # 将b中的元素添加到a中
    print(a)
    # insert
    a=[0,12]
    a.insert(1,6) #在元素为1的前面插入一个元素
    print(a)

    #print(namelist)

# 标识符
def test6():
    namelist = ['1','2','3']
    findName = input("input word")
    if( findName in namelist):
        print("存在")
    else:
        print("not int")
    pass


def test7():
    print("test7")
    namelist = ['1','2','3']
    del namelist[2]
    print(namelist)



if __name__ == '__main__':
    # print(keyword.kwlist)
    # num = 10
    # print(num)
    # num//=4  # 取整除赋值运算法 c//=a
    # print(num)
    # a =10
    # b =1
    # test1()
    # test2()
    # test3()
    # test4()
    # test5()
    # test6()
    test7()