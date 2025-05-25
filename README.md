# NBA Player Statistics System

A high-performance, real-time NBA player statistics logging and aggregation system built with microservices architecture. This system handles live game data ingestion, real-time aggregation, and provides APIs for fetching both live and historical player/team statistics.

## 🏀 Features

- **Real-time Data Logger**: Log NBA player statistics as games are happening
- **Live Statistics**: Fetch up-to-date stats even during ongoing games
- **Aggregate Analytics**: Calculate season averages for players and teams
- **High Throughput**: Handle hundreds of concurrent requests
- **Fault Tolerant**: Event-driven architecture with data durability
- **Scalable**: Horizontally scalable microservices design
- **Real-time Updates**: Statistics available immediately after ingestion

## 🏗️ Architecture
<a href="./skyhawk-nbastats/docs/system-design.drawio.png" target="_blank"> System Design (by draw.io) </a>
<br/>
<a href="./skyhawk-nbastats/docs/aws-design.drawio.png" target="_blank"/> AWS Architacture Design (by draw.io)</a>


### Data Flow

#### Write Path (Updating Player Statistics)
1. **External System** → API Gateway (rate limiting, auth)
2. **API Gateway** → Logger Service (validation, enrichment)
3. **Logger Service** → Kafka (reliable queuing)
4. **Kafka** → Live Aggregator (real-time processing)
5. **Live Aggregator** → Redis (live game cache with TTL)
6. **End Game Flusher** → PostgreSQL (final persistence on game end)

#### Read Path (Retrieving Player Statistics)
1. **Client Request** → API Gateway → Query API
2. **Query API** checks request type:
   - **Live Stats**: Query Redis for ongoing games + historical games: Query Redis for ongoing games -> if not exist: PostgreSQL for historical
   - **Historical Stats**: Query PostgreSQL directly
   - **Season Averages**: Query pre-aggregated tables in PostgreSQL
3. **Response**: Unified JSON format combining live and historical data

### Components

| Component | Purpose | Technology |
|-----------|---------|------------|
| **API Gateway** | Rate limiting, request routing | NGINX/Kong |
| **Logger Service** | Validate and queue stat events | Java + Kafka |
| **Live Aggregator** | Real-time stat aggregation | Java + Redis |
| **End Game Flusher** | Persist final stats to database | Java + PostgreSQL |
| **Query API** | Serve live and historical stats | Java |
| **Event Queue** | Reliable message processing | Kafka |
| **Cache Layer** | Live game data storage | Redis |
| **Database** | Historical data persistence | PostgreSQL |


## 🚀 Quick Start

### Prerequisites
- Docker & Docker Compose
- Java 17+ (for development)

### Run with Docker Compose
```bash
# Clone the repository
git clone https://github.com/Brurya-Gvirtz/skyhawk-nbastats.git
cd skyhawk-nbastats

# Start all services
docker-compose up -d

# Check service health
docker-compose ps
```

The system will be available at:
- **API Gateway**: http://localhost:8080
- **Logger API**: http://localhost:8081
- **Query API**: http://localhost:8082

## 📡 API Endpoints

### Logger API
```http
POST /api/v1/log-stats
Content-Type: application/json

{
  "gameId": "game_123",
  "playerId": "lebron_james",
  "teamId": "lakers",
  "timestamp": "2024-01-15T20:30:00Z",
  "stats": {
    "points": 28,
    "rebounds": 8,
    "assists": 12,
    "steals": 2,
    "blocks": 1,
    "fouls": 3,
    "turnovers": 4,
    "minutesPlayed": 38.5
  }
}
```

### Query API

#### Get Player Stats (Live + Historical)
```http
GET /api/v1/stats/player/{playerId}
```

#### Get Team Season Average
```http
GET /api/v1/stats/team/{teamId
```

### Response Format
```json
{
  "playerId": "lebron_james",
  "playerName": "LeBron James",
  "teamId": "lakers",
  "averages": {
    "points": 25.4,
    "rebounds": 7.8,
    "assists": 8.2,
    "steals": 1.3,
    "blocks": 0.8,
    "fouls": 2.1,
    "turnovers": 3.5,
    "minutesPlayed": 36.2
  },
  "gamesPlayed": 45,
  "lastUpdated": "2024-01-15T20:45:00Z"
}
```



### Testing
```bash
# Run unit tests
make test

# Run integration tests
make test-integration

# Load testing
make load-test

# Test coverage
make coverage
```

## ☁️ Cloud Deployment Architecture


### Production Infrastructure Components

#### Load Balancing & API Gateway
- **Application Load Balancer (ALB)**: Layer 7 load balancing with SSL termination
- **API Gateway**: Rate limiting, request routing, and authentication
- **Auto Scaling**: Based on CPU/memory utilization and request count

#### Container Orchestration
- **Amazon ECS with Fargate**: Serverless container management
- **Service Discovery**: AWS Cloud Map for service-to-service communication
- **Health Checks**: ALB health checks with automatic failover

#### Message Queue & Event Processing
- **Amazon MSK (Managed Kafka)**: 3-node cluster across AZs
- **Kafka Configuration**: 
  - 3-day retention for event replay capability
  - Partitioned by game_id for parallel processing
  - Replication factor: 3 for fault tolerance

#### Caching Layer
- **Amazon ElastiCache (Redis)**: 
  - Cluster mode enabled for horizontal scaling
  - Multi-AZ deployment for high availability
  - TTL-based eviction for live game data

#### Database Layer
- **Amazon RDS PostgreSQL**:
  - Multi-AZ deployment for 99.95% availability
  - Read replicas for query scaling
  - Automated backups and point-in-time recovery
  - Connection pooling with PgBouncer

#### Monitoring & Observability
- **CloudWatch**: Metrics, logs, and alarms
- **X-Ray**: Distributed tracing across services
- **Prometheus + Grafana**: Custom application metrics
- **ELK Stack**: Centralized logging and analysis

### Scalability Features
- **Horizontal Scaling**: ECS auto-scaling based on metrics
- **Database Scaling**: Read replicas and connection pooling
- **Cache Scaling**: Redis cluster mode with automatic sharding
- **Queue Scaling**: Kafka partitions scale consumer groups

### High Availability Features
- **Multi-AZ Deployment**: Services deployed across 3 availability zones
- **Auto-Recovery**: ECS automatically replaces failed containers
- **Circuit Breakers**: Prevent cascade failures between services
- **Graceful Degradation**: System continues with reduced functionality

### Security
- **VPC**: Private subnets for backend services
- **Security Groups**: Restrict network access between components
- **IAM Roles**: Least privilege access for each service
- **Secrets Manager**: Secure storage of database credentials and API keys

## 🤔 Implementation Challenges & Trade-offs

### Challenges Encountered

#### 1. Real-time Data Consistency
**Challenge**: Ensuring live statistics are immediately available while maintaining data consistency.

**Solution**: Implemented eventual consistency model with Redis as the source of truth for live data and PostgreSQL for historical accuracy.

**Trade-off**: Slight delay (< 1 second) between stat logging and availability in exchange for system reliability.

#### 2. High-Throughput Event Processing
**Challenge**: Processing thousands of stat updates per second during peak game times.

**Solution**: Used Kafka partitioning by game_id and horizontal scaling of consumer groups.

**Trade-off**: Increased infrastructure complexity in exchange for linear scalability.

#### 3. Live Game State Management
**Challenge**: Determining when games end to trigger final stat aggregation.

**Solution**: Hybrid approach using explicit "game end" events and inactivity timeouts.

**Trade-off**: Potential for delayed final stats if end events are missed, but system self-heals via timeouts.

### Architecture Decisions & Rationale

#### Event-Driven Architecture
**Why**: Decouples services, enables independent scaling, and provides audit trail.
**Trade-off**: Added complexity vs. simpler monolithic approach.

#### Redis for Live Data
**Why**: Sub-millisecond read latency for live statistics queries.
**Trade-off**: Additional infrastructure component vs. using database only.

#### Separate Read/Write APIs
**Why**: Optimized for different access patterns and scaling requirements.
**Trade-off**: More services to maintain vs. single API endpoint.
