#!/bin/bash

# JXC进销存管理系统前端部署脚本

set -e

# 配置变量
PROJECT_NAME="jxc-frontend"
BUILD_DIR="dist"
DOCKER_IMAGE_NAME="jxc-frontend"
DOCKER_TAG="latest"

echo "🚀 开始部署 $PROJECT_NAME..."

# 检查Node.js环境
check_node() {
    if ! command -v node &> /dev/null; then
        echo "❌ Node.js 未安装，请先安装 Node.js"
        exit 1
    fi
    
    if ! command -v npm &> /dev/null; then
        echo "❌ npm 未安装，请先安装 npm"
        exit 1
    fi
    
    echo "✅ Node.js 环境检查通过"
    node --version
    npm --version
}

# 安装依赖
install_dependencies() {
    echo "📦 安装项目依赖..."
    npm ci --only=production
    echo "✅ 依赖安装完成"
}

# 运行代码检查
run_lint() {
    echo "🔍 运行代码检查..."
    npm run lint:check
    npm run format:check
    echo "✅ 代码检查通过"
}

# 运行单元测试
run_tests() {
    echo "🧪 运行单元测试..."
    npm run test:run
    echo "✅ 单元测试通过"
}

# 构建项目
build_project() {
    echo "🔨 构建项目..."
    npm run build
    
    if [ ! -d "$BUILD_DIR" ]; then
        echo "❌ 构建失败，$BUILD_DIR 目录不存在"
        exit 1
    fi
    
    echo "✅ 项目构建完成"
    du -sh $BUILD_DIR
}

# 构建Docker镜像
build_docker_image() {
    echo "🐳 构建Docker镜像..."
    docker build -t $DOCKER_IMAGE_NAME:$DOCKER_TAG .
    echo "✅ Docker镜像构建完成"
    docker images | grep $DOCKER_IMAGE_NAME
}

# 启动服务
start_services() {
    echo "🚀 启动服务..."
    docker-compose up -d jxc-frontend
    echo "✅ 服务启动完成"
    
    # 等待服务启动
    sleep 5
    
    # 健康检查
    if curl -f http://localhost:3000/health > /dev/null 2>&1; then
        echo "✅ 健康检查通过"
    else
        echo "⚠️  健康检查失败，请检查服务状态"
    fi
}

# 清理旧版本
cleanup() {
    echo "🧹 清理旧版本..."
    
    # 清理悬空镜像
    if [ "$(docker images -f "dangling=true" -q)" ]; then
        docker rmi $(docker images -f "dangling=true" -q) || true
    fi
    
    # 清理构建缓存
    rm -rf node_modules/.cache || true
    
    echo "✅ 清理完成"
}

# 显示部署信息
show_info() {
    echo ""
    echo "🎉 部署完成！"
    echo "======================================"
    echo "项目名称: $PROJECT_NAME"
    echo "构建时间: $(date)"
    echo "访问地址: http://localhost:3000"
    echo "健康检查: http://localhost:3000/health"
    echo "======================================"
    echo ""
    echo "常用命令："
    echo "查看日志: docker-compose logs -f jxc-frontend"
    echo "停止服务: docker-compose stop jxc-frontend"
    echo "重启服务: docker-compose restart jxc-frontend"
    echo "查看状态: docker-compose ps"
    echo ""
}

# 主函数
main() {
    case "${1:-all}" in
        "check")
            check_node
            ;;
        "install")
            install_dependencies
            ;;
        "lint")
            run_lint
            ;;
        "test")
            run_tests
            ;;
        "build")
            build_project
            ;;
        "docker")
            build_docker_image
            ;;
        "start")
            start_services
            ;;
        "cleanup")
            cleanup
            ;;
        "all")
            check_node
            install_dependencies
            run_lint
            run_tests
            build_project
            build_docker_image
            start_services
            cleanup
            show_info
            ;;
        *)
            echo "使用方法: $0 [check|install|lint|test|build|docker|start|cleanup|all]"
            echo ""
            echo "参数说明："
            echo "  check   - 检查环境"
            echo "  install - 安装依赖"
            echo "  lint    - 代码检查"
            echo "  test    - 运行测试"
            echo "  build   - 构建项目"
            echo "  docker  - 构建镜像"
            echo "  start   - 启动服务"
            echo "  cleanup - 清理缓存"
            echo "  all     - 完整部署(默认)"
            exit 1
            ;;
    esac
}

# 执行主函数
main "$@"