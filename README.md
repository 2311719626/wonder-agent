# 编辑时长

此处反映的是通过 wakatime 统计的项目编辑时长，数据实时更新，仅供参考。

[![wakatime](https://wakatime.com/badge/user/5b960c5b-a7d7-4a2d-bb6b-fdcef6171837/project/b85d4212-d0f3-4d79-bcd3-a1b5d7ea9b36.svg)](https://wakatime.com/badge/user/5b960c5b-a7d7-4a2d-bb6b-fdcef6171837/project/b85d4212-d0f3-4d79-bcd3-a1b5d7ea9b36)

# 提交规范（Githook）

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

# 技术实现

## 1. 基础对话能力

用户 ===> LLM ===> 用户

LLM: 通过接入 Alibaba DashScope 提供的大模型，实现基础的对话能力，用户可以通过自然语言与 LLM 进行交互。

## 2. 对话记忆能力

会话 【上下文】 ===> LLM ===> 回答

会话窗口: 通过文件存储实现对话记忆，截取上下文时最近 8 条对话内容。

## 3. 总结报告能力

多轮对话 ===> LLM ===> 结构化输出 ===> 报告

结构化输出: 输出 json 格式，通过 LLM 实现多轮对话的总结，输出结构化数据。

## 4. 检索增强生成（RAG）

文档 ===> 向量化    --|

                    ||===> Count(余弦相似度) ===> 召回文本

用户提问 ===> 向量化 --|

向量化: 通过 PostgreSQL 插件实现向量嵌入、存储与检索召回。

## 5. 日志

用户 ===> （发送前） ===> LLM ===> （响应后） ===> 用户

自定义日志模块: 在用户请求发送前与 LLM 响应后记录日志。