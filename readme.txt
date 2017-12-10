1.	增加本地local调用的接口，暴露出generateId方法
2.	远程调用的时候，可以选择弱关联，提供默认adapter类，采用local generateId方法本地产生结果