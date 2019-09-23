# no-passwd settings for git push 


```
[core]
    repositoryformatversion = 0
    filemode = true
    bare = false
    logallrefupdates = true
[remote "origin"]
    url = https://gaocuiqun@github.com/gaocuiqun/user-management
    fetch = +refs/heads/*:refs/remotes/origin/*
[branch "master"]
    remote = origin
    merge = refs/heads/master
[credential]
    helper = store
```

