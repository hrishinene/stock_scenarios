class Vector:
    def __init__(self, x, y, z):
        self.x = x
        self.y = y
        self.z = z
        
    def magnitude(self):
        return (self.x**2 + self.y**2 + self.z**2) ** 0.5
    
    def get_unit_vector(self):
        mag = self.magnitude()
        if mag == 0:
            raise ValueError("Cannot compute unit vector of zero vector")
        return Vector(self.x / mag, self.y / mag, self.z / mag)
    
    def dot(self, other):
        return self.x * other.x + self.y * other.y + self.z * other.z
    
    def cross(self, other):
        return Vector(self.y * other.z - self.z * other.y,
                      self.z * other.x - self.x * other.z,
                      self.x * other.y - self.y * other.x)
        
    def add(self, other):
        return Vector(self.x + other.x, self.y + other.y, self.z + other.z)
    
    def subtract(self, other):
        return Vector(self.x - other.x, self.y - other.y, self.z - other.z)
    
    def scale(self, scalar):
        return Vector(self.x * scalar, self.y * scalar, self.z * scalar)
    
    def __repr__(self):
        return f"Vector({self.x}, {self.y}, {self.z})"

# Static unit vectors for common axes    
X = Vector(1, 0, 0)
Y = Vector(0, 1, 0)
Z = Vector(0, 0, 1)
    
    