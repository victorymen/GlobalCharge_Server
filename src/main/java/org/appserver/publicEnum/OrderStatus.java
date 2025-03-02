package org.appserver.publicEnum;

/**
 * 订单状态枚举
 */
public enum OrderStatus {

    UNPAID("UNPAID", "待支付") {
        @Override
        public boolean canTransferTo(OrderStatus targetStatus) {
            // 待支付可转为：已支付、取消（主动/超时）
            return targetStatus == PAID
                    || targetStatus == CANCELLED
                    || targetStatus == TIMEOUT_CANCELLED;
        }
    },
    CHARGEING("CHARGEING", "充值中") {
        @Override
        public boolean canTransferTo(OrderStatus targetStatus) {
            // 待支付可转为：已支付、取消（主动/超时）
            return targetStatus == PAID
                    || targetStatus == CANCELLED
                    || targetStatus == TIMEOUT_CANCELLED;
        }
    },
    PAID("PAID", "已支付") {
        @Override
        public boolean canTransferTo(OrderStatus targetStatus) {
            // 已支付可转为：发货中、取消（退款前）
            return targetStatus == DELIVERING
                    || targetStatus == CANCELLED
                    || targetStatus == REFUNDING;
        }
    },

    DELIVERING("DELIVERING", "发货中") {
        @Override
        public boolean canTransferTo(OrderStatus targetStatus) {
            // 发货中可转为：已完成、退款中
            return targetStatus == COMPLETED
                    || targetStatus == REFUNDING;
        }
    },

    COMPLETED("COMPLETED", "已完成") {
        @Override
        public boolean canTransferTo(OrderStatus targetStatus) {
            // 已完成是终态，不可再修改
            return false;
        }
    },

    CANCELLED("CANCELLED", "已取消") {
        @Override
        public boolean canTransferTo(OrderStatus targetStatus) {
            // 已取消是终态
            return false;
        }
    },

    TIMEOUT_CANCELLED("TIMEOUT_CANCELLED", "超时取消") {
        @Override
        public boolean canTransferTo(OrderStatus targetStatus) {
            // 超时取消是终态
            return false;
        }
    },

    REFUNDING("REFUNDING", "退款中") {
        @Override
        public boolean canTransferTo(OrderStatus targetStatus) {
            // 退款中可转为：已取消（退款失败）、已完成（退款成功）
            return targetStatus == CANCELLED
                    || targetStatus == COMPLETED;
        }
    };

    // 状态码（建议与枚举名一致）
    private final String code;
    // 状态描述
    private final String desc;

    OrderStatus(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 根据状态码获取枚举
     */
    public static OrderStatus fromCode(String code) {
        for (OrderStatus status : OrderStatus.values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("无效订单状态码: " + code);
    }

    /**
     * 校验是否允许转移到目标状态
     */
    public abstract boolean canTransferTo(OrderStatus targetStatus);

    // Getter
    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}