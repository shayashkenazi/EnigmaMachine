package DTOs;

public class DTO_ConsumerPrinter {
    private DTO_CodeDescription dto_codeDescription;
    private Long threadId;
    private String msgResult;

    public DTO_ConsumerPrinter(DTO_CodeDescription dto_codeDescription,String msgResult,Long threadId) {
        this.dto_codeDescription = dto_codeDescription;
        this.msgResult = msgResult;
        this.threadId = threadId;
    }

    public DTO_CodeDescription getDto_codeDescription() {
        return dto_codeDescription;
    }

    public Long getThreadId() {
        return threadId;
    }

    public String getMsgResult() {
        return msgResult;
    }
}