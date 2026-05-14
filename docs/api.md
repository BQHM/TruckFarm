# TruckFarm 接口设计

版本：v0.2
日期：2026-05-13
状态：接口草案

## 1. 接口约定

基础路径：

```text
/api
```

认证方式：

```http
Authorization: Bearer <access_token>
```

统一响应：

```json
{
  "code": 0,
  "message": "success",
  "data": {}
}
```

分页响应：

```json
{
  "code": 0,
  "message": "success",
  "data": {
    "records": [],
    "total": 100,
    "pageNum": 1,
    "pageSize": 10,
    "pages": 10
  }
}
```

通用分页查询参数：`pageNum`、`pageSize`、`sortField`、`sortOrder`。排序字段必须经过后端白名单校验。

通用状态码：

| code | 说明 |
| --- | --- |
| 0 | 成功 |
| 1001 | 请求参数错误 |
| 1004 | 资源不存在 |
| 1005 | 数据状态不允许当前操作 |
| 2001 | 用户不存在 |
| 2002 | 密码错误 |
| 2003 | 未登录或 Token 失效 |
| 2004 | 无权限 |
| 8001 | 库存不足 |
| 9001 | 账户余额不足或资金状态异常 |
| 9999 | 系统异常 |

## 2. 认证接口

### 2.1 登录

```http
POST /api/auth/login
```

请求：

```json
{
  "username": "admin",
  "password": "123456",
  "captchaId": "uuid",
  "captchaCode": "abcd"
}
```

响应：

```json
{
  "accessToken": "xxx",
  "refreshToken": "xxx",
  "expiresIn": 7200
}
```

### 2.2 认证相关接口

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| POST | `/api/auth/refresh` | 刷新 Token |
| POST | `/api/auth/logout` | 退出登录 |
| GET | `/api/auth/me` | 当前用户信息 |
| GET | `/api/auth/menus` | 当前用户菜单树 |
| GET | `/api/auth/permissions` | 当前用户按钮权限 |

当前用户信息响应示例：

```json
{
  "userId": 1,
  "username": "admin",
  "nickname": "系统管理员",
  "roles": ["ADMIN"],
  "permissions": ["crop:create", "sales:ship", "simulation:tick"]
}
```

## 3. 用户权限接口

### 3.1 用户

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| GET | `/api/users` | 用户分页，支持 username、status、departmentId 查询 |
| POST | `/api/users` | 创建用户 |
| GET | `/api/users/{id}` | 用户详情 |
| PUT | `/api/users/{id}` | 更新用户 |
| PATCH | `/api/users/{id}/status` | 启用或禁用用户 |
| PATCH | `/api/users/{id}/password` | 重置密码 |
| PUT | `/api/users/{id}/roles` | 分配角色 |

### 3.2 角色

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| GET | `/api/roles` | 角色分页 |
| POST | `/api/roles` | 创建角色 |
| GET | `/api/roles/{id}` | 角色详情 |
| PUT | `/api/roles/{id}` | 更新角色 |
| DELETE | `/api/roles/{id}` | 删除角色 |
| PUT | `/api/roles/{id}/menus` | 分配菜单权限 |

### 3.3 菜单

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| GET | `/api/menus/tree` | 菜单树 |
| POST | `/api/menus` | 创建菜单 |
| PUT | `/api/menus/{id}` | 更新菜单 |
| DELETE | `/api/menus/{id}` | 删除菜单 |

## 4. 组织员工接口

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| GET | `/api/departments` | 部门列表或分页 |
| POST | `/api/departments` | 创建部门 |
| PUT | `/api/departments/{id}` | 更新部门 |
| PATCH | `/api/departments/{id}/status` | 启用或禁用部门 |
| GET | `/api/employees` | 员工分页 |
| POST | `/api/employees` | 创建员工 |
| PUT | `/api/employees/{id}` | 更新员工 |
| PATCH | `/api/employees/{id}/status` | 员工状态变更 |

## 5. 作物接口

### 5.1 作物分类

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| GET | `/api/crop-categories` | 分类列表 |
| POST | `/api/crop-categories` | 创建分类 |
| PUT | `/api/crop-categories/{id}` | 更新分类 |
| DELETE | `/api/crop-categories/{id}` | 删除分类 |

### 5.2 作物

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| GET | `/api/crops` | 作物分页，支持 name、categoryId、status 查询 |
| POST | `/api/crops` | 创建作物 |
| GET | `/api/crops/{id}` | 作物详情 |
| PUT | `/api/crops/{id}` | 更新作物 |
| PATCH | `/api/crops/{id}/status` | 启用或禁用作物 |
| GET | `/api/crops/{id}/growth-rules` | 查询作物生长规则 |
| PUT | `/api/crops/{id}/growth-rules` | 更新作物生长规则 |

创建作物请求：

```json
{
  "name": "番茄",
  "categoryId": 1,
  "growthCycleDays": 90,
  "referencePrice": 4.50,
  "unit": "kg",
  "description": "适合春季种植"
}
```

作物生长规则请求示例：

```json
{
  "seasonCode": "SPRING",
  "temperatureMin": 18,
  "temperatureMax": 28,
  "humidityMin": 55,
  "humidityMax": 75,
  "growthRateFactor": 1.1,
  "diseaseRiskFactor": 0.9,
  "qualityFactor": 1.05
}
```

## 6. 地块接口

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| GET | `/api/fields` | 地块分页，支持 name、status 查询 |
| POST | `/api/fields` | 创建地块 |
| GET | `/api/fields/{id}` | 地块详情 |
| PUT | `/api/fields/{id}` | 更新地块 |
| PATCH | `/api/fields/{id}/status` | 更新地块状态 |
| GET | `/api/fields/{id}/environment` | 查询地块最新环境状态 |

## 7. 种植计划接口

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| GET | `/api/planting-plans` | 种植计划分页 |
| POST | `/api/planting-plans` | 创建种植计划 |
| GET | `/api/planting-plans/{id}` | 计划详情 |
| PUT | `/api/planting-plans/{id}` | 更新待开始计划 |
| PATCH | `/api/planting-plans/{id}/start` | 开始种植 |
| PATCH | `/api/planting-plans/{id}/harvest` | 完成收获 |
| PATCH | `/api/planting-plans/{id}/cancel` | 取消计划 |
| GET | `/api/planting-plans/{id}/growth-states` | 查询生长状态时间序列 |

## 8. 采购接口

### 8.1 供应商

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| GET | `/api/suppliers` | 供应商分页 |
| POST | `/api/suppliers` | 创建供应商 |
| PUT | `/api/suppliers/{id}` | 更新供应商 |
| PATCH | `/api/suppliers/{id}/status` | 启用或禁用供应商 |

### 8.2 采购单

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| GET | `/api/procurement-orders` | 采购单分页 |
| POST | `/api/procurement-orders` | 创建采购单 |
| GET | `/api/procurement-orders/{id}` | 采购单详情 |
| PATCH | `/api/procurement-orders/{id}/inbound` | 采购入库 |
| PATCH | `/api/procurement-orders/{id}/cancel` | 取消采购单 |

## 9. 库存接口

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| GET | `/api/inventory/stocks` | 库存分页，支持 cropId、alertOnly 查询 |
| GET | `/api/inventory/flows` | 库存流水分页 |
| POST | `/api/inventory/adjustments` | 盘点调整 |
| PATCH | `/api/inventory/stocks/{id}/alert-threshold` | 修改预警阈值 |

## 10. 销售接口

### 10.1 客户

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| GET | `/api/customers` | 客户分页 |
| POST | `/api/customers` | 创建客户 |
| PUT | `/api/customers/{id}` | 更新客户 |
| PATCH | `/api/customers/{id}/status` | 启用或禁用客户 |

### 10.2 销售订单

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| GET | `/api/sales-orders` | 销售单分页 |
| POST | `/api/sales-orders` | 创建销售单 |
| GET | `/api/sales-orders/{id}` | 销售单详情 |
| PATCH | `/api/sales-orders/{id}/ship` | 销售出库 |
| PATCH | `/api/sales-orders/{id}/cancel` | 取消销售单 |

## 11. 资金接口

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| GET | `/api/finance/accounts/current` | 查询当前经营账户 |
| GET | `/api/finance/flows` | 资金流水分页 |
| POST | `/api/finance/adjustments` | 手工调整资金 |
| GET | `/api/finance/summary` | 查询收入、成本、余额摘要 |

资金流水响应项示例：

```json
{
  "id": 1,
  "flowType": "EXPENSE",
  "amount": 3200.00,
  "bizType": "PROCUREMENT",
  "bizNo": "PO20260513001",
  "summary": "采购番茄种子",
  "balanceAfter": 46800.00,
  "createdAt": "2026-05-13 10:30:00"
}
```

## 12. 首页沙盘接口

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| GET | `/api/dashboard/summary` | 首页沙盘概览指标 |
| GET | `/api/dashboard/farm-state` | 当前日期、天气、风险和经营状态 |
| GET | `/api/dashboard/sales-trend?months=6` | 销售趋势 |
| GET | `/api/dashboard/fund-trend?days=30` | 资金趋势 |
| GET | `/api/dashboard/crop-sales-ranking?limit=10` | 作物销售排行 |
| GET | `/api/dashboard/todos` | 待办事项 |

首页沙盘响应示例：

```json
{
  "simulationDate": "2026-05-13",
  "season": "SPRING",
  "weather": {
    "type": "CLOUDY",
    "temperature": 24.5,
    "humidity": 68
  },
  "overview": {
    "plantingArea": 120.50,
    "activePlanCount": 8,
    "inventoryAlertCount": 4,
    "currentBalance": 52300.00,
    "monthlyRevenue": 35600.00,
    "monthlyCost": 12800.00
  },
  "riskSummary": {
    "warningCount": 3,
    "dangerCount": 1
  }
}
```

## 13. 数字沙盘模拟接口

### 13.1 模拟日历与 Tick

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| GET | `/api/simulation/calendar` | 查询当前模拟日期、季节和轮次 |
| POST | `/api/simulation/ticks/day` | 推进一天 |
| GET | `/api/simulation/ticks` | 查询时间推进日志 |

推进一天响应示例：

```json
{
  "simulationDate": "2026-05-14",
  "weather": {
    "type": "RAINY",
    "temperature": 18.5,
    "humidity": 88
  },
  "events": [
    {
      "eventType": "WEATHER",
      "level": "WARNING",
      "title": "连续阴雨",
      "description": "地块湿度升高，病虫害风险上升。"
    }
  ],
  "fundImpact": {
    "maintenanceCost": 120.00
  }
}
```

### 13.2 经营动作

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| POST | `/api/simulation/actions/procurement` | 发起带模拟结果的采购动作 |
| POST | `/api/simulation/actions/sowing` | 播种 |
| POST | `/api/simulation/actions/watering` | 浇水 |
| POST | `/api/simulation/actions/fertilizing` | 施肥 |
| POST | `/api/simulation/actions/pest-control` | 除虫 |
| POST | `/api/simulation/actions/harvest` | 收获并生成入库结果 |
| POST | `/api/simulation/actions/sales` | 销售并生成成交结果 |
| GET | `/api/simulation/actions/{id}` | 查询动作详情 |

采购动作响应示例：

```json
{
  "actionId": 1001,
  "outcomeId": 2001,
  "summary": "本次采购优品率 84%，损耗率 4%，预计延迟 1 天。",
  "finalResult": {
    "premiumQuantity": 84.00,
    "normalQuantity": 12.00,
    "lossQuantity": 4.00,
    "delayDays": 1,
    "procurementAmount": 3200.00
  },
  "fundEffect": {
    "flowType": "EXPENSE",
    "amount": 3200.00,
    "balanceAfter": 46800.00
  },
  "explanation": "连续阴雨导致运输湿度偏高，种子受潮风险上升。"
}
```

### 13.3 模拟结果与事件

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| GET | `/api/simulation/outcomes` | 查询模拟结果列表 |
| GET | `/api/simulation/outcomes/{id}` | 查询模拟结果详情，包含规则、AI 修正、随机种子 |
| POST | `/api/simulation/outcomes/{id}/replay` | 使用相同上下文和随机种子复现结果 |
| GET | `/api/events` | 查询事件时间线 |
| GET | `/api/events/{id}` | 查询事件详情 |

模拟结果详情响应示例：

```json
{
  "outcomeId": 2001,
  "actionType": "PROCUREMENT",
  "simulationDate": "2026-05-13",
  "inputContext": {
    "weather": "RAINY",
    "supplierReliability": 82,
    "marketTension": "HIGH"
  },
  "baseResult": {
    "premiumRateRange": [0.78, 0.94],
    "lossRateRange": [0.01, 0.05]
  },
  "aiModifier": {
    "premiumRateDelta": -0.04,
    "lossRateDelta": 0.02,
    "reason": "连续阴雨导致运输湿度偏高"
  },
  "finalResult": {
    "premiumQuantity": 84,
    "normalQuantity": 12,
    "lossQuantity": 4
  },
  "randomSeed": 84523911,
  "aiStatus": "SUCCESS"
}
```

### 13.4 天气、市场、资金与复盘

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| GET | `/api/weather/records` | 查询天气记录 |
| GET | `/api/market/prices` | 查询市场价格 |
| GET | `/api/reports/operation/current` | 查询当前经营复盘草稿 |
| POST | `/api/reports/operation/generate` | 生成本轮经营复盘 |
| GET | `/api/reports/operation/{id}` | 查询经营复盘详情 |

## 14. 系统接口

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| GET | `/api/dicts/{dictType}` | 查询字典数据 |
| GET | `/api/dict-types` | 字典类型列表 |
| POST | `/api/dict-types` | 创建字典类型 |
| POST | `/api/dict-data` | 创建字典数据 |
| GET | `/api/system/operation-logs` | 操作日志分页 |

## 15. 后续补充

正式开发时每个接口需要继续补充请求字段校验规则、完整响应字段、权限编码、错误码、示例 curl 和 OpenAPI 注解。
