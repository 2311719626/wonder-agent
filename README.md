# 编辑时长

此处反映的是通过 wakatime 统计的项目编辑时长，数据实时更新，仅供参考。

[![wakatime](https://wakatime.com/badge/user/5b960c5b-a7d7-4a2d-bb6b-fdcef6171837/project/b85d4212-d0f3-4d79-bcd3-a1b5d7ea9b36.svg)](https://wakatime.com/badge/user/5b960c5b-a7d7-4a2d-bb6b-fdcef6171837/project/b85d4212-d0f3-4d79-bcd3-a1b5d7ea9b36)

# 项目提交（Githook）

在执行提交前通过以下的脚本实现提交信息的校验，提交信息格式必须为 `"<类型>: <描述>"`，例如 `Feat: 新增功能`，可选类型为 `Feat|Update|FixBug|Docs|Style|Refactor|Perfect|Test|Chore`。

```bash
#!/bin/bash

# 获取提交信息
COMMIT_MSG_FILE=$1
COMMIT_MSG=$(cat "$COMMIT_MSG_FILE")

# 正则表达式
regex="^(Feat|Update|FixBug|Docs|Style|Refactor|Perfect|Test|Chore): .+"

# 匹配提交类型
if [[ "$COMMIT_MSG" =~ $regex ]]; then
# 根据类型映射 Emoji
case "${BASH_REMATCH[1]}" in
"Feat")      EMOJI="✨" ;;
"Update")    EMOJI="🔄" ;;
"FixBug")    EMOJI="🐛" ;;
"Docs")      EMOJI="📝" ;;
"Style")     EMOJI="🎨" ;;
"Refactor")  EMOJI="♻️" ;;
"Perfect")   EMOJI="⚡" ;;
"Test")      EMOJI="✅" ;;
"Chore")     EMOJI="🔧" ;;
*)           EMOJI="" ;;
esac

    # 追加 Emoji（如果未添加过）
    if [[ ! "$COMMIT_MSG" =~ "$EMOJI" ]]; then
        NEW_MSG="${BASH_REMATCH[0]} $EMOJI${COMMIT_MSG:${#BASH_REMATCH[0]}}"
        echo "$NEW_MSG" > "$COMMIT_MSG_FILE"
    fi
else
# 拒绝提交
echo "错误：提交信息格式必须为 \"<类型>: <描述>\"，例如 \"Feat: 新增功能\""
echo "可选类型: Feat|Update|FixBug|Docs|Style|Refactor|Perfect|Test|Chore"
exit 1
fi
```



