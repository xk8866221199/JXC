#!/bin/bash

# JXC进销存管理系统 - MySQL管理脚本
# MySQL 8.0.43 企业级数据库管理工具

MYSQL_HOME="/opt/homebrew/opt/mysql@8.0"
MYSQL_DATA_DIR="/opt/homebrew/var/mysql"
MYSQL_USER="root"
MYSQL_PASSWORD="root123"
MYSQL_DATABASE="jxc_db"

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# 打印彩色日志
log_info() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

log_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

log_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

log_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# 检查MySQL是否正在运行
check_mysql_status() {
    if pgrep -f "mysqld" > /dev/null; then
        log_success "MySQL服务正在运行"
        return 0
    else
        log_warning "MySQL服务未运行"
        return 1
    fi
}

# 启动MySQL服务
start_mysql() {
    log_info "启动MySQL服务..."
    if check_mysql_status; then
        log_warning "MySQL服务已在运行中"
        return 0
    fi
    
    # 启动MySQL
    $MYSQL_HOME/bin/mysqld_safe --datadir=$MYSQL_DATA_DIR &
    
    # 等待服务启动
    sleep 3
    
    if check_mysql_status; then
        log_success "MySQL服务启动成功"
        return 0
    else
        log_error "MySQL服务启动失败"
        return 1
    fi
}

# 停止MySQL服务
stop_mysql() {
    log_info "停止MySQL服务..."
    if ! check_mysql_status; then
        log_warning "MySQL服务未运行"
        return 0
    fi
    
    # 停止MySQL
    $MYSQL_HOME/bin/mysqladmin -u$MYSQL_USER -p$MYSQL_PASSWORD shutdown
    
    # 等待服务停止
    sleep 2
    
    if ! check_mysql_status; then
        log_success "MySQL服务停止成功"
        return 0
    else
        log_error "MySQL服务停止失败"
        return 1
    fi
}

# 重启MySQL服务
restart_mysql() {
    log_info "重启MySQL服务..."
    stop_mysql
    sleep 2
    start_mysql
}

# 连接到MySQL
connect_mysql() {
    log_info "连接到MySQL数据库..."
    $MYSQL_HOME/bin/mysql -u$MYSQL_USER -p$MYSQL_PASSWORD $MYSQL_DATABASE
}

# 验证MySQL连接
test_connection() {
    log_info "测试MySQL连接..."
    if $MYSQL_HOME/bin/mysql -u$MYSQL_USER -p$MYSQL_PASSWORD -e "SELECT VERSION(), USER(), NOW();" 2>/dev/null; then
        log_success "MySQL连接测试成功"
        return 0
    else
        log_error "MySQL连接测试失败"
        return 1
    fi
}

# 查看数据库状态
show_status() {
    log_info "MySQL服务状态信息："
    echo "=================================================="
    
    # 检查服务状态
    if check_mysql_status; then
        echo "✅ 服务状态: 运行中"
        
        # 获取详细信息
        VERSION=$($MYSQL_HOME/bin/mysql -u$MYSQL_USER -p$MYSQL_PASSWORD -e "SELECT VERSION();" 2>/dev/null | tail -1)
        UPTIME=$($MYSQL_HOME/bin/mysql -u$MYSQL_USER -p$MYSQL_PASSWORD -e "SHOW STATUS LIKE 'Uptime';" 2>/dev/null | tail -1 | awk '{print $2}')
        CONNECTIONS=$($MYSQL_HOME/bin/mysql -u$MYSQL_USER -p$MYSQL_PASSWORD -e "SHOW STATUS LIKE 'Threads_connected';" 2>/dev/null | tail -1 | awk '{print $2}')
        
        echo "📦 版本信息: $VERSION"
        echo "⏰ 运行时间: $UPTIME 秒"
        echo "🔗 连接数量: $CONNECTIONS"
        echo "🏠 安装目录: $MYSQL_HOME"
        echo "📁 数据目录: $MYSQL_DATA_DIR"
        echo "👤 用户名称: $MYSQL_USER"
        echo "🗄️  数据库名: $MYSQL_DATABASE"
        
        # 显示数据库和表
        echo ""
        log_info "数据库列表："
        $MYSQL_HOME/bin/mysql -u$MYSQL_USER -p$MYSQL_PASSWORD -e "SHOW DATABASES;" 2>/dev/null
        
        echo ""
        log_info "JXC数据库表列表："
        $MYSQL_HOME/bin/mysql -u$MYSQL_USER -p$MYSQL_PASSWORD $MYSQL_DATABASE -e "SHOW TABLES;" 2>/dev/null
        
    else
        echo "❌ 服务状态: 未运行"
    fi
    
    echo "=================================================="
}

# 备份数据库
backup_database() {
    log_info "备份JXC数据库..."
    BACKUP_DIR="/Users/xukai/Documents/CODE/JXC/backup"
    mkdir -p $BACKUP_DIR
    
    BACKUP_FILE="$BACKUP_DIR/jxc_db_backup_$(date +%Y%m%d_%H%M%S).sql"
    
    if $MYSQL_HOME/bin/mysqldump -u$MYSQL_USER -p$MYSQL_PASSWORD $MYSQL_DATABASE > $BACKUP_FILE 2>/dev/null; then
        log_success "数据库备份完成: $BACKUP_FILE"
        ls -lh $BACKUP_FILE
    else
        log_error "数据库备份失败"
        return 1
    fi
}

# 显示帮助信息
show_help() {
    echo "JXC进销存管理系统 - MySQL管理脚本"
    echo "=================================================="
    echo "使用方法: $0 [命令]"
    echo ""
    echo "可用命令:"
    echo "  start     - 启动MySQL服务"
    echo "  stop      - 停止MySQL服务" 
    echo "  restart   - 重启MySQL服务"
    echo "  status    - 查看服务状态"
    echo "  connect   - 连接到数据库"
    echo "  test      - 测试连接"
    echo "  backup    - 备份数据库"
    echo "  help      - 显示帮助信息"
    echo ""
    echo "数据库配置:"
    echo "  MySQL版本: 8.0.43"
    echo "  安装路径: $MYSQL_HOME"
    echo "  数据目录: $MYSQL_DATA_DIR"
    echo "  用户名: $MYSQL_USER"
    echo "  密码: $MYSQL_PASSWORD"
    echo "  数据库: $MYSQL_DATABASE"
    echo "=================================================="
}

# 主函数
case "$1" in
    start)
        start_mysql
        ;;
    stop)
        stop_mysql
        ;;
    restart)
        restart_mysql
        ;;
    status)
        show_status
        ;;
    connect)
        connect_mysql
        ;;
    test)
        test_connection
        ;;
    backup)
        backup_database
        ;;
    help|--help|-h)
        show_help
        ;;
    *)
        log_error "未知命令: $1"
        echo ""
        show_help
        exit 1
        ;;
esac