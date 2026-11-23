CREATE EXTENSION IF NOT EXISTS vector;
CREATE EXTENSION IF NOT EXISTS hstore;

CREATE TABLE IF NOT EXISTS vector_store (
    id TEXT PRIMARY KEY,
    content TEXT,
    metadata JSONB,
    embedding VECTOR(768)
);

-- Recreate/align embedding column dimension if table already exists with a different dimension
DO $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM information_schema.columns 
        WHERE table_name = 'vector_store' AND column_name = 'embedding'
    ) THEN
        BEGIN
            ALTER TABLE vector_store ALTER COLUMN embedding TYPE VECTOR(768);
        EXCEPTION WHEN OTHERS THEN
            DROP TABLE IF EXISTS vector_store CASCADE;
            CREATE TABLE vector_store (
                id TEXT PRIMARY KEY,
                content TEXT,
                metadata JSONB,
                embedding VECTOR(768)
            );
        END;
    END IF;
END $$;

-- Create HNSW index for fast search
CREATE INDEX IF NOT EXISTS vector_store_embedding_idx ON vector_store USING HNSW (embedding vector_cosine_ops);
